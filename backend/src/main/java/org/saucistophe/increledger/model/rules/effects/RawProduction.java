package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saucistophe.increledger.model.rules.GameRules;

@RegisterForReflection
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawProduction implements Effect {

  @NotBlank String resource;

  @DecimalMin("0")
  double amount;

  @Override
  public boolean isValid(GameRules gameRules) {
    return gameRules.getResourceById(resource) != null;
  }
}
