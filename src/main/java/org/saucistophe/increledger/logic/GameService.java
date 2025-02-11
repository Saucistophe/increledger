package org.saucistophe.increledger.logic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.GameDto;
import org.saucistophe.increledger.model.effects.UnlockOccupation;
import org.saucistophe.increledger.model.rules.GameRules;
import org.saucistophe.increledger.model.rules.NamedEntity;
import org.saucistophe.increledger.model.rules.Occupation;
import org.saucistophe.increledger.model.rules.Population;

@Singleton
public class GameService extends GameComputingService {

  protected ObjectMapper objectMapperForSignature;

  public GameService(
      GameRules gameRules, CryptoService cryptoService, ActionsVisitor actionsVisitor) {
    super(gameRules, cryptoService, actionsVisitor);
  }

  void startup(@Observes StartupEvent event) {
    // Custom dedicated object mapper for the specific case of serializing and signing
    objectMapperForSignature =
        JsonMapper.builder().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true).build();
    // Ignore empty properties, so that new ones can be added without messing with existing games
    objectMapperForSignature.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
  }

  public GameDto newGame() {
    var game = new Game();
    game.setPopulations(
        gameRules.getPopulations().stream()
            .collect(Collectors.toMap(NamedEntity::getName, Population::getInitialCount)));
    var resultGameDto = new GameDto();
    resultGameDto.setGame(game);
    resultGameDto.setSignature(cryptoService.sign(toJson(game)));

    return resultGameDto;
  }

  // TODO revamp with effects?
  public List<String> getAvailableOccupations(Game game) {
    var unlockedInitially =
        gameRules.getOccupations().stream()
            .filter(Occupation::isUnlocked)
            .map(Occupation::getName)
            .toList();
    var unlockedThroughTech =
        game.getTechs().keySet().stream()
            .map(
                t ->
                    gameRules.getTechs().stream()
                        .filter(ct -> ct.getName().equals(t))
                        .findFirst()
                        .orElseThrow())
            .flatMap(t -> t.getEffects().stream())
            .filter(UnlockOccupation.class::isInstance)
            .map(UnlockOccupation.class::cast)
            .map(UnlockOccupation::getOccupation)
            .toList();
    return Stream.concat(unlockedInitially.stream(), unlockedThroughTech.stream()).toList();
  }

  public GameDto process(GameDto gameDto) {
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

      // TODO handle caps
      var existingAmount = game.getResources().getOrDefault(resource, 0.);
      var newAmount = existingAmount + amount * ellapsedMillis / 1000.;
      game.getResources().put(resource, newAmount);
    }

    for (var action : actions) {
      if (!action.acceptValidation(actionsVisitor, game)) {
        Log.error("Invalid action: " + action);
        throw new BadRequestException("Invalid action");
      }
      action.acceptExecution(actionsVisitor, game);
    }

    // Sign the game
    var resultGameDto = new GameDto();
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
