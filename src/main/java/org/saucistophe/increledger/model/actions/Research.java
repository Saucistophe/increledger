package org.saucistophe.increledger.model.actions;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saucistophe.increledger.logic.ActionsVisitor;
import org.saucistophe.increledger.model.Game;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Research implements Action {

  @NotBlank String tech;

  @Override
  public boolean acceptValidation(ActionsVisitor visitor, Game game) {
    return visitor.isValid(this, game);
  }

  @Override
  public void acceptExecution(ActionsVisitor visitor, Game game) {
    visitor.execute(this, game);
  }
}
