package org.saucistophe.increledger.model.rules;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Population extends NamedEntityWithEffects {

  @Min(0) private long initialCount = 0;
  @Min(0) private long initialCap = 0;
}
