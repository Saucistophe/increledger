package org.saucistophe.increledger.model.effects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class IncreaseCap implements Effect {

  @Min(1)
  long amount;

  @NotEmpty
  String target;
}
