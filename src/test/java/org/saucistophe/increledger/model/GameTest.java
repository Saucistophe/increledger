package org.saucistophe.increledger.model;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.saucistophe.increledger.model.occupations.Occupation;
import org.saucistophe.increledger.model.resources.Resource;

class GameTest {

  @Test
  void serialize() throws JsonProcessingException {
    Game game = new Game();
    game.setPopulation(1234L);
    game.setTimestamp(1234L);
    game.getOccupations().put(Occupation.WOOD_CUTTER, 12L);
    game.getResources().put(Resource.WOOD, 1234.);
    assertEquals(
        """
    {
      "maxPopulation" : 5,
      "population" : 1234,
      "occupations" : {
        "woodCutter" : 12
      },
      "resources" : {
        "wood" : 1234.0
      },
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
    "maxPopulation" : 5,
    "population" : 1234,
    "occupations" : {
      "woodCutter" : 12
    },
    "resources" : {
      "wood" : 1234.0
    },
    "timestamp" : 1234
  }""");

    assertEquals(1234L, game.getPopulation());
    assertEquals(Map.of(Occupation.WOOD_CUTTER, 12L), game.getOccupations());
  }
}
