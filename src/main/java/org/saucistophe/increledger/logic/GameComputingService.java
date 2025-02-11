package org.saucistophe.increledger.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.effects.BoostProduction;
import org.saucistophe.increledger.model.effects.Effect;
import org.saucistophe.increledger.model.effects.IncreasePopulation;
import org.saucistophe.increledger.model.effects.RawProduction;
import org.saucistophe.increledger.model.rules.GameRules;
import org.saucistophe.increledger.model.rules.NamedEntityWithEffects;

/** Service used only for presenting the game's state in a more computing-friendly manner. */
@RequiredArgsConstructor
public class GameComputingService {
  protected record EffectRecord<T extends Effect>(String source, T effect, Long count) {}

  protected final GameRules gameRules;
  protected final CryptoService cryptoService;
  protected final ActionsVisitor actionsVisitor;
  protected ObjectMapper objectMapperForSignature;

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

  public Map<String, Long> getCurrentPopulation(Game game) {
    var populationIncreases =
      getEffectsFromEntities(game.getTechs(), gameRules::getTechById, IncreasePopulation.class);
    populationIncreases.addAll(
      getEffectsFromEntities(
        game.getOccupations(), gameRules::getOccupationById, IncreasePopulation.class));
    return populationIncreases.stream()
      .collect(
        Collectors.toMap(
          r -> r.effect.getPopulation(), r -> r.count * r.effect.getAmount(), Long::sum));
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
