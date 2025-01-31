package org.saucistophe.increledger.model.effects;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.saucistophe.increledger.model.Game;

@Data
public class UnlockOccupation implements Effect {

  @NotBlank String occupation;

  @Override
  public void execute(Game game) {
    // No-op: The unlocking will be done when asking for available occupations
  }
}
