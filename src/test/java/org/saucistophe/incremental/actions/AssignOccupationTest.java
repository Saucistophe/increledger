package org.saucistophe.incremental.actions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.saucistophe.incremental.Game;
import org.saucistophe.incremental.occupations.Woodcutter;

class AssignOccupationTest {

  @Test
  void isValid() {

    Game game = new Game();
    var action = new AssignOccupation(Woodcutter.builder().numbersOfAssignees(10).build());

    assertFalse(action.isValid(game));

    game.setPopulation(10);
    assertTrue(action.isValid(game));

    game.setPopulation(14);
    game.getOccupations().add(Woodcutter.builder().numbersOfAssignees(5).build());
    assertFalse(action.isValid(game));

    game.setPopulation(15);
    assertTrue(action.isValid(game));
    }

  @Test
  void execute() {
    Game game = new Game();
    game.setPopulation(10);
    var action = new AssignOccupation(Woodcutter.builder().numbersOfAssignees(3).build());

    action.execute(game);
    assertEquals(List.of(Woodcutter.builder().numbersOfAssignees(3).build()), game.getOccupations());
    assertEquals(7, game.getFreePopulation());
    }
}