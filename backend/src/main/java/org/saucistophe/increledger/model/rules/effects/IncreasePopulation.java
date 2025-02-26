package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.saucistophe.increledger.model.rules.GameRules;

@RegisterForReflection
@Data
public class IncreasePopulation implements Effect {

  @Min(1)
  long amount;

  @NotEmpty String target;

  @Override
  public boolean isValid(GameRules gameRules) {
    return gameRules.getPopulationById(target) != null;
  }
}
