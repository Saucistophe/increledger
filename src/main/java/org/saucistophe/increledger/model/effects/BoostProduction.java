package org.saucistophe.increledger.model.effects;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BoostProduction implements Effect {

  @NotBlank String target;

  @DecimalMin("0")
  double multiplier;
}
