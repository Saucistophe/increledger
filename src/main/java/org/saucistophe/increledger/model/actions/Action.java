package org.saucistophe.increledger.model.actions;

import org.saucistophe.increledger.model.Game;

public interface Action {
  boolean isValid(Game game);
  void execute(Game game);

}
