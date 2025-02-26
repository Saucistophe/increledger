package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.saucistophe.increledger.model.rules.GameRules;

@RegisterForReflection
@Data
public class IncreaseCap implements Effect {

  @Min(1)
  long amount;

  @NotEmpty String target; // Can boost a resource, a tech, or a population.

  @Override
  public boolean isValid(GameRules gameRules) {
    return gameRules.getResourceById(target) != null
        || gameRules.getTechById(target) != null
        || gameRules.getOccupationById(target) != null;
  }
}
