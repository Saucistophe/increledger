package org.saucistophe.incremental;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.saucistophe.incremental.occupations.Woodcutter;
import org.saucistophe.incremental.resources.Wood;

class GameTest {

  @Test
  void serialize() throws JsonProcessingException {
    Game game = new Game();
    game.setPopulation(1234L);
    game.setTimestamp(1234L);
    game.getOccupations().add(Woodcutter.builder().numbersOfAssignees(12).build());
    game.getResources().add(Wood.builder().amount(1234).build());
    assertEquals(
        """
    {
      "population" : 1234,
      "occupations" : [ {
        "type" : "Woodcutter",
        "numbersOfAssignees" : 12
      } ],
      "resources" : [ {
        "type" : "Wood",
        "amount" : 1234
      } ],
      "timestamp" : 1234
    }""",
        game.toJson());
  }

  @Test
  void deserialize() throws JsonProcessingException {
    var game =
        Game.fromJson(
            """
  {
    "population" : 1234,
    "occupations" : [ {
      "type" : "Woodcutter",
      "numbersOfAssignees" : 12
    } ],
    "resources" : [ {
      "type" : "Wood",
      "amount" : 1234
    } ],
    "timestamp" : 1234
  }""");

    assertEquals(1234L, game.getPopulation());
    assertEquals(
        List.of(Woodcutter.builder().numbersOfAssignees(12).build()), game.getOccupations());
  }

}
