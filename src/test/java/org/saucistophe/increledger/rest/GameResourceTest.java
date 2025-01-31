package org.saucistophe.increledger.rest;

import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.saucistophe.increledger.logic.GameService;
import org.saucistophe.increledger.model.actions.AssignOccupation;

@QuarkusTest
class GameResourceTest {

  @Inject GameService gameService;

  @Test
  void getNewGame() {
    var gameDto = gameService.newGame();
    assertEquals("Dummy Signature", gameDto.getSignature());
    assertEquals(List.of(), gameDto.getActions());
    assertEquals(5, gameDto.getGame().getMaxPopulation());
    assertEquals(2, gameDto.getGame().getPopulation());
    assertTrue(gameDto.getGame().getTimestamp() > 1738364824582L);
  }

  @Test
  void processGame() {
    var gameDto = gameService.newGame();
    gameDto.getActions().add(new AssignOccupation("woodcutter", 1L));
    gameDto = gameService.process(gameDto);
    // Production not yet started
    assertFalse(gameDto.getGame().getResources().containsKey("wood"));

    CompletableFuture.runAsync(() -> {}, CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS)).join();
    gameDto = gameService.process(gameDto);
    var currentWood = gameDto.getGame().getResources().get("wood");
    assertTrue(currentWood > 0.);
  }

  @Test
  void getProduction() {
    var gameDto = gameService.newGame();
    assertEquals(Map.of(), gameService.getCurrentProduction(gameDto.getGame()));

    gameDto.getActions().add(new AssignOccupation("woodcutter", 1L));
    gameDto = gameService.process(gameDto);
    assertEquals(Map.of("wood", 1.), gameService.getCurrentProduction(gameDto.getGame()));
  }

  @Test
  void getAvailableOccupations() {
    var gameDto = gameService.newGame();
    assertEquals(List.of("woodcutter"), gameService.getAvailableOccupations(gameDto.getGame()));

    gameDto.getGame().getTechs().add("quarry_workers");

    assertEquals(
        List.of("woodcutter", "quarry_worker"),
        gameService.getAvailableOccupations(gameDto.getGame()));
  }
}
