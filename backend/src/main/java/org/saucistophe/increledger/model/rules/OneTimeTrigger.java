package org.saucistophe.increledger.model.rules;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@RegisterForReflection
@EqualsAndHashCode(callSuper = true)
@Data
public class OneTimeTrigger extends EntityWithOneTimeEffects {

  @NotBlank private String flag;

  @NotEmpty Map<String, Double> prerequisites;

  @Override
  public boolean isValid(GameRules gameRules) {
    return true;
  }
}
