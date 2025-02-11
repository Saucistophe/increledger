package org.saucistophe.increledger.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.effects.*;
import org.saucistophe.increledger.model.rules.GameRules;
import org.saucistophe.increledger.model.rules.NamedEntityWithEffects;
import org.saucistophe.increledger.model.rules.Population;

/** Service used only for presenting the game's state in a more computing-friendly manner. */
@RequiredArgsConstructor
public class GameComputingService {
  protected record EffectRecord<T extends Effect>(String source, T effect, Long count) {}

  protected final GameRules gameRules;
  protected final CryptoService cryptoService;
  protected final ActionsVisitor actionsVisitor;

  public Map<String, Double> getCurrentProduction(Game game) {
    Map<String, Double> result = new HashMap<>();

    var boosts = getBoosts(game);
    var rawProductions =
        getEffectsFromEntities(game.getTechs(), gameRules::getTechById, RawProduction.class);
    rawProductions.addAll(
        getEffectsFromEntities(
            game.getOccupations(), gameRules::getOccupationById, RawProduction.class));

    for (var production : rawProductions) {
      var resource = production.effect.getResource();
      var amount = production.effect.getAmount() * production.count;
      // Boost due to techs and occupations
      if (boosts.containsKey(production.source)) amount *= boosts.get(production.source);
      // Boost due to boosting the whole resource production
      if (boosts.containsKey(resource)) amount *= boosts.get(resource);

      result.putIfAbsent(resource, 0.);
      result.put(resource, result.get(resource) + amount);
    }

    return result;
  }

  public Map<String, Long> getPopulationCaps(Game game) {

    Map<String, Long> caps = new HashMap<>();

    for (Population population : gameRules.getPopulations()) {
      caps.put(population.getName(), population.getInitialCap());
    }

    // TODO handle separate boosts for caps and pop? Handle boosts here? Create a method that
    // retrieves boosted effects?
    // TODO Handle unlimited caps
    var capIncreases =
        getEffectsFromEntities(game.getTechs(), gameRules::getTechById, IncreaseCap.class);
    capIncreases.addAll(
        getEffectsFromEntities(
            game.getOccupations(), gameRules::getOccupationById, IncreaseCap.class));
    for (var capIncrease : capIncreases) {
      caps.computeIfPresent(
          capIncrease.effect.getTarget(),
          (k, v) -> v + capIncrease.count * capIncrease.effect.getAmount());
    }

    return caps;
  }

  public Map<String, Long> getCurrentPopulation(Game game) {

    Map<String, Long> result = new HashMap<>();

    for (Population population : gameRules.getPopulations()) {
      result.put(population.getName(), population.getInitialCount());
    }

    var populationIncreases =
        getEffectsFromEntities(game.getTechs(), gameRules::getTechById, IncreasePopulation.class);
    populationIncreases.addAll(
        getEffectsFromEntities(
            game.getOccupations(), gameRules::getOccupationById, IncreasePopulation.class));
    for (var populationIncrease : populationIncreases) {
      result.computeIfPresent(
          populationIncrease.effect.getTarget(),
          (k, v) -> v + populationIncrease.count * populationIncrease.effect.getAmount());
    }

    var caps = getPopulationCaps(game);
    for (Population population : gameRules.getPopulations()) {
      result.computeIfPresent(
          population.getName(), (k, v) -> Math.min(v, caps.get(population.getName())));
    }

    return result;
  }

  public Map<String, Long> getFreePopulations(Game game) {
    var result = getCurrentPopulation(game);
    for (var gameOccupation : game.getOccupations().entrySet()) {
      var occupationName = gameOccupation.getKey();
      var occupation = gameRules.getOccupationById(occupationName);

      result.computeIfPresent(
          occupationName, (k, v) -> v - occupation.getAmountNeeded() * gameOccupation.getValue());
    }
    return result;
  }

  protected Map<String, Double> getBoosts(Game game) {
    Map<String, Double> result = new HashMap<>();

    // Merge occupation and tech effects
    var allEffects =
        getEffectsFromEntities(game.getTechs(), gameRules::getTechById, BoostProduction.class);
    allEffects.addAll(
        getEffectsFromEntities(
            game.getOccupations(), gameRules::getOccupationById, BoostProduction.class));

    while (!allEffects.isEmpty()) {
      // Find boosts that are not subject to other boosts
      var boostTargets = allEffects.stream().map(b -> b.effect.getTarget()).distinct().toList();
      var sourceEffects =
          allEffects.stream().filter(b -> !boostTargets.contains(b.source)).toList();

      for (var sourceEffect : sourceEffects) {
        result.putIfAbsent(sourceEffect.effect.getTarget(), 1.);
        result.put(
            sourceEffect.effect.getTarget(),
            result.get(sourceEffect.effect.getTarget()) * sourceEffect.effect.getMultiplier());
      }

      allEffects.removeAll(sourceEffects);
    }
    return result;
  }

  protected <T extends Effect> List<EffectRecord<T>> getEffectsFromEntities(
      Map<String, Long> entities,
      Function<String, ? extends NamedEntityWithEffects> lookupFunction,
      Class<T> targetClass) {
    List<EffectRecord<T>> result = new ArrayList<>();
    for (var entry : entities.entrySet()) {
      var entityName = entry.getKey();
      var entity = lookupFunction.apply(entityName);
      var effects = entity.getEffects();
      for (var effect : effects) {
        if (effect.getClass().isAssignableFrom(targetClass))
          result.add(new EffectRecord<>(entityName, targetClass.cast(effect), entry.getValue()));
      }
    }

    return result;
  }
}
