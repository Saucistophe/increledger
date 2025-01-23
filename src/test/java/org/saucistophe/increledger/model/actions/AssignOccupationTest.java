package org.saucistophe.increledger.model.actions;

import static org.junit.jupiter.api.Assertions.*;
import static org.saucistophe.increledger.model.occupations.Occupation.*;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.saucistophe.increledger.model.Game;

class AssignOccupationTest {

  @Test
  void isValid() {

    Game game = new Game();
    var action = new AssignOccupation(WOOD_CUTTER, 10L);

    assertFalse(action.isValid(game));

    game.setPopulation(10);
    assertTrue(action.isValid(game));

    game.setPopulation(14);
    game.getOccupations().put(WOOD_CUTTER, 5L);
    assertFalse(action.isValid(game));

    game.setPopulation(15);
    assertTrue(action.isValid(game));
  }

  @Test
  void execute() {
    Game game = new Game();
    game.setPopulation(10);
    var action = new AssignOccupation(WOOD_CUTTER, 3L);

    action.execute(game);
    assertEquals(Map.of(WOOD_CUTTER, 3L), game.getOccupations());
    assertEquals(7, game.getFreePopulation());
  }
}
