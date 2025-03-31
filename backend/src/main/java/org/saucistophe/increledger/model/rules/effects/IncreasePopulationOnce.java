package org.saucistophe.increledger.model.rules.effects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.saucistophe.increledger.logic.OneTimeEffectVisitor;
import org.saucistophe.increledger.model.Game;

@RegisterForReflection
@Data
public class IncreasePopulationOnce implements OneTimeEffect {

  @Min(1)
  long amount;

  @NotEmpty String target;

  @Override
  public boolean acceptValidation(OneTimeEffectVisitor visitor) {
    return visitor.isValid(this);
  }

  @Override
  public void acceptExecution(OneTimeEffectVisitor visitor, Game game) {
    visitor.execute(this, game);
  }
}
