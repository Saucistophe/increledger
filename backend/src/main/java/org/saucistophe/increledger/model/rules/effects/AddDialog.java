package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saucistophe.increledger.logic.OneTimeEffectVisitor;
import org.saucistophe.increledger.model.Game;

@RegisterForReflection
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDialog implements OneTimeEffect {

  @NotBlank String dialog;

  @Override
  public boolean acceptValidation(OneTimeEffectVisitor visitor) {
    return visitor.isValid(this);
  }

  @Override
  public void acceptExecution(OneTimeEffectVisitor visitor, Game game) {
    visitor.execute(this, game);
  }
}
