package org.saucistophe.increledger.model.rules;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.EqualsAndHashCode;

@RegisterForReflection
@EqualsAndHashCode(callSuper = true)
@Data
public class Resource extends NamedEntity {
  @DecimalMin("1")
  private double cap;

  private String emoji;

  @Override
  public boolean isValid(GameRules gameRules) {
    return true;
  }
}
