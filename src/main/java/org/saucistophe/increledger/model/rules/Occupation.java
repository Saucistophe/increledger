package org.saucistophe.increledger.model.rules;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Occupation extends NamedEntityWithEffects{
  @NotBlank private String population;
  @Min(1) private long amountNeeded = 1;
  private boolean unlocked = false;
  
}
