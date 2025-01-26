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
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.dto.GameDTO;
import org.saucistophe.increledger.model.dto.GameRules;

@ApplicationScoped
@RequiredArgsConstructor
public class GameService {

  private final GameRules gameRules;
  private final CryptoService cryptoService;
  private ObjectMapper objectMapperForSignature;

  void startup(@Observes StartupEvent event) {
    // Custom dedicated object mapper for the specific case of serializing and signing
    objectMapperForSignature = new ObjectMapper();
    // Ignore empty properties, so that new ones can be added without messing with existing games
    objectMapperForSignature.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
  }

  public GameDTO newGame() {
    var game = new Game();
    var resultGameDto = new GameDTO();
    resultGameDto.setGame(game);
    resultGameDto.setSignature(cryptoService.sign(toJson(game)));

    return resultGameDto;
  }

  public Map<String, Double> getCurrentProduction(Game game) {
    Map<String, Double> currentProduction = new HashMap<>();
    for (var entry : game.getOccupations().entrySet()) {
      var occupationId = entry.getKey();
      var occupation = gameRules.getOccupationById(occupationId);
      var numberOfAssignees = entry.getValue();

      for (var producedResource : occupation.getResourcesProduced().entrySet()) {
        var resource = producedResource.getKey();
        var amount = producedResource.getValue();

        var existingAmount = currentProduction.getOrDefault(resource, 0.);
        var newAmount = existingAmount + amount * numberOfAssignees;
        currentProduction.put(resource, newAmount);
      }
    }
    return currentProduction;
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
    var ellapsedMillis = currentTime - previousTime;

    var actions = gameDto.getActions();
    if (actions == null) {
      actions = new ArrayList<>();
    }

    // First pass the time
    var production = getCurrentProduction(game);
    for (var producedResource : production.entrySet()) {
      var resource = producedResource.getKey();
      var amount = producedResource.getValue();

      var existingAmount = game.getResources().getOrDefault(resource, 0.);
      var newAmount = existingAmount + amount * ellapsedMillis / 1000.;
      game.getResources().put(resource, newAmount);
    }

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
      return objectMapperForSignature.writeValueAsString(game);
    } catch (JsonProcessingException e) {
      Log.error("Could not serialize game", e);
      throw new InternalServerErrorException(e);
    }
  }
}
