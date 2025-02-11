package org.saucistophe.increledger.model.effects;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawProduction implements Effect {

  @NotBlank String resource;

  @DecimalMin("0")
  double amount;
}
