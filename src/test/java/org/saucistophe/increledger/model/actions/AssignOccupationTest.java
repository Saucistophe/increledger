package org.saucistophe.increledger.model.actions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.rules.GameRules;

class AssignOccupationTest {

  GameRules gameRules = new GameRules();

  @Test
  void isValid() {

    Game game = new Game();
    var action = new AssignOccupation("woodCutter", 10L);
    // TODO Move this to the validator test class
    /* assertFalse(action.isValid(game, gameRules));

    game.setPopulation(10);
    assertTrue(action.isValid(game, gameRules));

    game.setPopulation(14);
    game.getOccupations().put("woodCutter", 5L);
    assertFalse(action.isValid(game, gameRules));

    game.setPopulation(15);
    assertTrue(action.isValid(game, gameRules));*/
  }

  @Test
  void execute() {
    // TODO Move this to the validator test class
    /*Game game = new Game();
    game.setPopulation(10);
    var action = new AssignOccupation("woodCutter", 3L);

    action.execute(game);
    assertEquals(Map.of("woodCutter", 3L), game.getOccupations());
    assertEquals(7, game.getFreePopulation());*/
  }
}
