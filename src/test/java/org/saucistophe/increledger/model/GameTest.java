package org.saucistophe.increledger.model;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GameTest {

  @Test
  void serialize() throws JsonProcessingException {
    var mapper = new ObjectMapper();
    Game game = new Game();
    game.setPopulation(1234L);
    game.setTimestamp(1234L);
    game.getOccupations().put("woodCutter", 12L);
    game.getResources().put("wood", 1234.);
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
      "techs" : [ ],
      "timestamp" : 1234
    }""",
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(game));
  }

  @Test
  void deserialize() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    var game =
        mapper.readValue(
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
            Game.class);

    assertEquals(1234L, game.getPopulation());
    assertEquals(Map.of("woodCutter", 12L), game.getOccupations());
  }
}
