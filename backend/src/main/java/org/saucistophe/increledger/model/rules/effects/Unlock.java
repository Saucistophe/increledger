package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.saucistophe.increledger.model.rules.GameRules;

@RegisterForReflection
@Data
public class Unlock implements Effect {

  @NotBlank String target;

  @Override
  public boolean isValid(GameRules gameRules) {
    return gameRules.getTechById(target) != null;
  }
}
