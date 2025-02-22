package org.saucistophe.increledger.model.rules;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@RegisterForReflection
@EqualsAndHashCode(callSuper = true)
@Data
public class Occupation extends NamedEntityWithEffects {
  @NotBlank private String population;

  @Min(1)
  private long populationNeeded = 1;

  @Min(-1)
  private long cap = -1; // A cap of -1 means unlimited
}
