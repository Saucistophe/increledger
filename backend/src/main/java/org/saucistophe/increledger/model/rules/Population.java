package org.saucistophe.increledger.model.rules;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@RegisterForReflection
@EqualsAndHashCode(callSuper = true)
@Data
public class Population extends NamedEntityWithEffects {

  @Min(0)
  private long initialCount = 0;

  @Min(-1)
  private long cap = 0;

  @Override
  public boolean isValid(GameRules gameRules) {
    return effects == null || effects.stream().allMatch(e -> e.isValid(gameRules));
  }
}
