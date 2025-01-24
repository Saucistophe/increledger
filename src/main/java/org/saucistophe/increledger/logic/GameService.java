package org.saucistophe.increledger.logic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.GameDTO;
import org.saucistophe.increledger.model.actions.PassTheTime;

@ApplicationScoped
@RequiredArgsConstructor
public class GameService {

  private final CryptoService cryptoService;
  private ObjectMapper objectMapper;

  void startup(@Observes StartupEvent event) {
    // Custom dedicated object mapper for the specific case of serializing and signing
    objectMapper = new ObjectMapper();
    // Ignore empty properties, so that new ones can be added without messing with existing games
    objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
  }

  public GameDTO newGame() {
    var game = new Game();
    var resultGameDto = new GameDTO();
    resultGameDto.setGame(game);
    resultGameDto.setSignature(cryptoService.sign(toJson(game)));

    return resultGameDto;
  }

  public GameDTO process(GameDTO gameDto) {
    // Check signature
    var verification = cryptoService.verify(toJson(gameDto.getGame()), gameDto.getSignature());
    if (!verification) {
      System.err.println("Invalid signature");
      throw new BadRequestException("Invalid signature");
    }

    var game = gameDto.getGame();
    var previousTime = game.getTimestamp();
    game.updateTimestamp();
    var currentTime = game.getTimestamp();

    var actions = gameDto.getActions();
    if (actions == null) {
      actions = new ArrayList<>();
    }
    // First pass the time
    actions.addFirst(new PassTheTime(currentTime - previousTime));
    for (var action : actions) {
      if (!action.isValid(game)) {
        Log.error("Invalid action: " + action);
        throw new BadRequestException("Invalid action");
      }
      action.execute(game);
    }

    // Sign the game
    var resultGameDto = new GameDTO();
    resultGameDto.setGame(game);
    resultGameDto.setSignature(cryptoService.sign(toJson(game)));

    return resultGameDto;
  }

  private String toJson(Game game) {
    try {
      return objectMapper.writeValueAsString(game);
    } catch (JsonProcessingException e) {
      Log.error("Could not serialize game", e);
      throw new InternalServerErrorException(e);
    }
  }
}
