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
public class AddMessage implements OneTimeEffect {

  @NotBlank String message; // Todo add translations

  @Override
  public boolean isValid(GameRules gameRules) {
    return true;
  }

  @Override
  public void applyEffect(Game game) {
    game.getMessages().add(message);
  }
}
