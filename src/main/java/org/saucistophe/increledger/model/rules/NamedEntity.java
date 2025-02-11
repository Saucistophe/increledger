package org.saucistophe.increledger.model.rules;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public abstract class NamedEntity {
  @NotBlank private String name;
}
