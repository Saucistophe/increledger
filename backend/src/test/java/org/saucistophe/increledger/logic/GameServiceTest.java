package org.saucistophe.increledger.logic;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.node.NullNode;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;
import org.saucistophe.increledger.model.Game;

@QuarkusTest
class GameServiceTest {
  @Inject GameService gameService;

  @Test
  void getTimeSpent() {
    var translation = NullNode.instance;
    var game = new Game();
    game.setTimestamp(1_000_000L);
    game.setCreatedAt(0L);

    gameService.gameRules.setTimeUnits(new LinkedHashMap<>());
    gameService.gameRules.getTimeUnits().put("seconds", 1);
    assertEquals("1000 seconds", gameService.getTimeSpent(game, translation));

    gameService.gameRules.getTimeUnits().put("minutes", 60);
    assertEquals("16 minutes 40 seconds", gameService.getTimeSpent(game, translation));

    gameService.gameRules.getTimeUnits().put("hours", 60);
    assertEquals("16 minutes 40 seconds", gameService.getTimeSpent(game, translation));

    game.setTimestamp(3_601_000L);
    assertEquals("1 hours 1 seconds", gameService.getTimeSpent(game, translation));

    assertEquals(
        "1 heures 1 secondes",
        gameService.getTimeSpent(game, gameService.gameRules.getTranslations().at("/fr")));
  }
}
