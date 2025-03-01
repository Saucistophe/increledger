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
    var resource = gameRules.getResourceById(target);
    var tech = gameRules.getTechById(target);
    var occupation = gameRules.getOccupationById(target);

    if (resource == null && tech == null && occupation == null) return false;

    if (resource != null && resource.getCap() == -1) return false;
    if (occupation != null && occupation.getCap() == -1) return false;

    return true;
  }
}
