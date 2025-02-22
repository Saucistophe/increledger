package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@RegisterForReflection
@Data
public class IncreaseCap implements Effect {

  @Min(1)
  long amount;

  @NotEmpty String target;
}
