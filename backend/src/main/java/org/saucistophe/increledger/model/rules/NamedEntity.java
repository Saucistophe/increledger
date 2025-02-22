package org.saucistophe.increledger.model.rules;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@RegisterForReflection
@Data
public abstract class NamedEntity {
  @NotBlank private String name;
}
