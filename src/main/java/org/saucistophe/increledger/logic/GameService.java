package org.saucistophe.increledger.logic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.GameDto;
import org.saucistophe.increledger.model.effects.UnlockOccupation;
import org.saucistophe.increledger.model.rules.GameRules;
import org.saucistophe.increledger.model.rules.Occupation;

@ApplicationScoped
@RequiredArgsConstructor
public class GameService {

  private final GameRules gameRules;
  private final CryptoService cryptoService;
  private final ActionsVisitor actionsVisitor;
  private ObjectMapper objectMapperForSignature;

  void startup(@Observes StartupEvent event) {
    // Custom dedicated object mapper for the specific case of serializing and signing
    objectMapperForSignature =
        JsonMapper.builder().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true).build();
    // Ignore empty properties, so that new ones can be added without messing with existing games
    objectMapperForSignature.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
  }

  public GameDto newGame() {
    var game = new Game();
    var resultGameDto = new GameDto();
    resultGameDto.setGame(game);
    resultGameDto.setSignature(cryptoService.sign(toJson(game)));

    return resultGameDto;
  }

  public List<String> getAvailableOccupations(Game game) {
    var unlockedInitially =
        gameRules.getOccupations().stream()
            .filter(Occupation::isUnlocked)
            .map(Occupation::getName)
            .toList();
    var unlockedThroughTech =
        game.getTechs().stream()
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
