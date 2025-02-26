package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.saucistophe.increledger.model.rules.GameRules;

@RegisterForReflection
@Data
public class BoostProduction implements Effect {

  @NotBlank String target; // Can boost a resource, a tech, or a population.

  @DecimalMin("0")
  double multiplier;

  @Override
  public boolean isValid(GameRules gameRules) {
    return gameRules.getResourceById(target) != null
        || gameRules.getTechById(target) != null
        || gameRules.getOccupationById(target) != null;
  }
}
