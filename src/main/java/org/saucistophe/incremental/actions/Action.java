package org.saucistophe.incremental.actions;

import org.saucistophe.incremental.Game;

public interface Action {
  public boolean isValid(Game game);
  public void execute(Game game);

}
