package org.saucistophe.increledger.model.actions;

import static org.junit.jupiter.api.Assertions.*;
import static org.saucistophe.increledger.model.occupations.Occupation.WOOD_CUTTER;

import org.junit.jupiter.api.Test;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.resources.Resource;

class PassTheTimeTest {

  @Test
  void isValid() {
    var action = new PassTheTime(12);
    assertTrue(action.isValid(new Game()));

    action = new PassTheTime(0);
    assertFalse(action.isValid(new Game()));
  }

  @Test
  void execute() {

    Game game = new Game();
    game.getOccupations().put(WOOD_CUTTER, 1L);

    assertEquals(0, game.getResources().size());

    var action = new PassTheTime(1000);
    action.execute(game);

    assertEquals(1, game.getResources().size());
    assertEquals(1., game.getResources().get(Resource.WOOD), 0.01);

    action = new PassTheTime(2000);
    action.execute(game);

    assertEquals(1, game.getResources().size());
    assertEquals(3., game.getResources().get(Resource.WOOD), 0.01);

    game.getOccupations().put(WOOD_CUTTER, 3L);
    action = new PassTheTime(1000);
    action.execute(game);

    assertEquals(1, game.getResources().size());
    assertEquals(6., game.getResources().get(Resource.WOOD), 0.01);
  }
}
