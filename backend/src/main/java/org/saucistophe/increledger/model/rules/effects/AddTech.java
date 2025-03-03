package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Min;
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
public class AddTech implements OneTimeEffect {

  @NotBlank String tech;

  @Min(1)
  long count;

  @Override
  public boolean isValid(GameRules gameRules) {
    return gameRules.getTechById(tech) != null;
  }

  @Override
  public void applyEffect(Game game) {
    game.getTechs().putIfAbsent(tech, 0L);
    game.getTechs().put(tech, game.getTechs().get(tech) + count);
  }
}
