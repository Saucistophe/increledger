package org.saucistophe.increledger.model.effects;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.saucistophe.increledger.model.Game;

@Data
public class ProductionMultiplier implements Effect {

  @NotBlank String resource;

  @DecimalMin("0")
  double multiplier;

  @Override
  public void execute(Game game) {}
}
