package org.saucistophe.increledger.model.rules;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Min;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@RegisterForReflection
@EqualsAndHashCode(callSuper = true)
@Data
public class Tech extends NamedEntityWithEffects {

  private Map<String, Double> cost;

  // TODO handle counts and caps
  @Min(-1)
  private long initialCap = -1; // A cap of -1 means unlimited uses; ok for buildings.

  @Override
  public boolean isValid(GameRules gameRules) {

    return cost.keySet().stream().allMatch(k -> gameRules.getResourceById(k) != null)
        && cost.values().stream().allMatch(e -> e > 0)
        && effects.stream().allMatch(e -> e.isValid(gameRules));
  }
}
