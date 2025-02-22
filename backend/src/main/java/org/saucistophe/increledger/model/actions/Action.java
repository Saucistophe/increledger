package org.saucistophe.increledger.model.actions;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.saucistophe.increledger.logic.ActionsVisitor;
import org.saucistophe.increledger.model.Game;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
public interface Action {
  boolean acceptValidation(ActionsVisitor validityVisitor, Game game);

  void acceptExecution(ActionsVisitor visitor, Game game);
}
