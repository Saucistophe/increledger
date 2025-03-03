package org.saucistophe.increledger.model.rules;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@RegisterForReflection
@EqualsAndHashCode(callSuper = true)
@Data
public class DialogChoice extends EntityWithOneTimeEffects {
  @NotBlank private String name;

  @Override
  public boolean isValid(GameRules gameRules) {
    return true;
  }
}
