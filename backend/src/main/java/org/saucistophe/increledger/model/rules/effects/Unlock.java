package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@RegisterForReflection
@Data
public class Unlock implements Effect {

  @NotBlank String target;
}
