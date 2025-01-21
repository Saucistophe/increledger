package org.saucistophe.increledger.model.actions;

import org.saucistophe.increledger.model.Game;

public interface Action {
  public boolean isValid(Game game);
  public void execute(Game game);

}
