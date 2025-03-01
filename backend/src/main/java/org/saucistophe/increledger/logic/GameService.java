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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.GameDescription;
import org.saucistophe.increledger.model.GameDto;
import org.saucistophe.increledger.model.rules.GameRules;
import org.saucistophe.increledger.model.rules.NamedEntity;
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

    return process(resultGameDto);
  }

  public GameDescription getDescription(Game game) {
    var result = new GameDescription();

    // Keep only resources with actual production, or more than 0 resource.
    var production = getCurrentProduction(game);
    var resourcesNames =
        game.getResources().entrySet().stream()
            .filter(e -> e.getValue() > 0)
            .map(Map.Entry::getKey);
    var producedResourcesNames = production.keySet().stream();
    var relevantResources =
        Stream.concat(resourcesNames, producedResourcesNames).distinct().toList();

    result.setResources(new ArrayList<>());
    var resourcesCaps = this.getResourcesCaps(game);
    for (var resource : gameRules.getResources()) {
      if (relevantResources.contains(resource.getName())) {
        result
            .getResources()
            .add(
                new GameDescription.ResourceDto(
                    resource.getName(),
                    game.getResources().get(resource.getName()),
                    resourcesCaps.get(resource.getName()),
                    production.getOrDefault(resource.getName(), 0.),
                    resource.getEmoji()));
      }
    }

    result.setTechs(new ArrayList<>());
    var techs = game.getTechs();
    var techCaps = getTechCaps(game);
    techs.forEach(
        (techName, techCount) -> {
          var tech = gameRules.getTechById(techName);
          result
              .getTechs()
              .add(
                  new GameDescription.TechDto(
                      techName,
                      techCount,
                      techCaps.get(techName),
                      tech.getCost())); // TODO handle exponential cost
        });
    result.setPopulations(new ArrayList<>());
    var availablePopulations = game.getPopulations();
    var availableOccupations = game.getOccupations();
    var populationsCaps = getPopulationCaps(game);

    var freePopulations = getFreePopulations(game);

    availablePopulations.forEach(
        (populationName, populationCount) -> {
          List<GameDescription.OccupationDto> occupations = new ArrayList<>();
          availableOccupations.forEach(
              (occupationName, occupationCount) -> {
                var occupation = gameRules.getOccupationById(occupationName);
                if (occupation.getPopulation().equals(populationName)) {
                  occupations.add(
                      new GameDescription.OccupationDto(
                          occupationName,
                          occupationCount,
                          occupation.getCap())); // TODO handle occupations caps...
                }
              });
          result
              .getPopulations()
              .add(
                  new GameDescription.PopulationDto(
                      populationName,
                      populationCount,
                      populationsCaps.get(populationName),
                      freePopulations.get(populationName),
                      occupations));
        });

    sortAccordingToRules(result);

    return result;
  }

  private void sortAccordingToRules(GameDescription result) {
    result
        .getResources()
        .sort(
            Comparator.comparingInt(
                r -> gameRules.getResources().indexOf(gameRules.getResourceById(r.name()))));
    result
        .getPopulations()
        .sort(
            Comparator.comparingInt(
                r -> gameRules.getPopulations().indexOf(gameRules.getPopulationById(r.name()))));
    for (var population : result.getPopulations()) {
      population
          .occupations()
          .sort(
              Comparator.comparingInt(
                  r -> gameRules.getOccupations().indexOf(gameRules.getOccupationById(r.name()))));
    }
    result
        .getTechs()
        .sort(
            Comparator.comparingInt(
                r -> gameRules.getTechs().indexOf(gameRules.getTechById(r.name()))));
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
    var resourceCaps = getResourcesCaps(game);
    var production = getCurrentProduction(game);
    for (var producedResource : production.entrySet()) {
      var resource = producedResource.getKey();
      var amount = producedResource.getValue();

      var existingAmount = game.getResources().getOrDefault(resource, 0.);
      var newAmount = existingAmount + amount * ellapsedMillis / 1000.;
      newAmount = Math.min(newAmount, resourceCaps.get(resource));
      game.getResources().put(resource, newAmount);
    }

    // Add here everything that was unlocked from prerequisites, with amount 0
    for (var tech : gameRules.getTechs()) {
      if (game.hasAny(tech.getPrerequisites())) game.getTechs().putIfAbsent(tech.getName(), 0L);
    }
    for (var occupation : gameRules.getOccupations()) {
      if (game.hasAny(occupation.getPrerequisites()))
        game.getOccupations().putIfAbsent(occupation.getName(), 0L);
    }
    for (var population : gameRules.getPopulations()) {
      if (game.hasAny(population.getPrerequisites()))
        game.getPopulations().putIfAbsent(population.getName(), 0L);
    }

    // Fix the resources: If a production just started, you should still have the resource, even if
    // just at zero.
    production = getCurrentProduction(game);
    for (var producedResource : production.keySet())
      game.getResources().putIfAbsent(producedResource, 0.);

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
    resultGameDto.setGameDescription(getDescription(game));

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
