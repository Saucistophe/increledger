package org.saucistophe;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.saucistophe.incremental.Game;
import org.saucistophe.incremental.actions.AssignOccupation;
import org.saucistophe.incremental.occupations.Woodcutter;

public class Main {
  public static void main(String[] args) throws JsonProcessingException {

    Game game = new Game();
    System.out.println(game);

    var action = new AssignOccupation(1, Woodcutter.class);

    if (action.isValid(game)) {
      action.execute(game);
      game.updateTimestamp();
    } else System.out.println("Invalid action");

  }
}
