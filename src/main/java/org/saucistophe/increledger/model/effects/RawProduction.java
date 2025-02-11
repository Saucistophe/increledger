package org.saucistophe.increledger.model.effects;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RawProduction implements Effect {

  @NotBlank String resource;

  @DecimalMin("0")
  double amount;
}
