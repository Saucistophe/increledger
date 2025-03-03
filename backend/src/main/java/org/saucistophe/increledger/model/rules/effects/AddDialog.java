package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.rules.GameRules;

@RegisterForReflection
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDialog implements OneTimeEffect {

  @NotBlank String dialog;

  @Override
  public boolean isValid(GameRules gameRules) {
    return gameRules.getDialogById(dialog) != null;
  }

  @Override
  public void applyEffect(Game game) {
    game.getDialogs().add(dialog);
  }
}
