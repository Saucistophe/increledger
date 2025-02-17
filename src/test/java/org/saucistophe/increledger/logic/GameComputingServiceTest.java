package org.saucistophe.increledger.logic;

import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.saucistophe.increledger.model.Game;

@QuarkusTest
class GameComputingServiceTest {

  @Inject GameComputingService gameComputingService;

  @Test
  void getCurrentProduction() {
    var game = new Game();
    var currentProduction = gameComputingService.getCurrentProduction(game);
    assertEquals(Map.of(), currentProduction);

    game.getOccupations().put("woodcutter", 1L);
    currentProduction = gameComputingService.getCurrentProduction(game);
    assertEquals(Map.of("wood", 1.), currentProduction);

    game.getOccupations().put("woodcutter", 2L);
    currentProduction = gameComputingService.getCurrentProduction(game);
    assertEquals(Map.of("wood", 2.), currentProduction);

    game.getOccupations().clear();
    // Researching a new occupation should not yield any production.
    game.getTechs().put("quarry_workers", 1L);
    currentProduction = gameComputingService.getCurrentProduction(game);
    assertEquals(Map.of(), currentProduction);

    game.getOccupations().clear();
    // Having zero of a resource should not make the null production appear
    game.getResources().put("wood", 1.);
    currentProduction = gameComputingService.getCurrentProduction(game);
    assertEquals(Map.of(), currentProduction);
  }

  @Test
  void getResourcesCaps() {}

  @Test
  void getTechCaps() {}

  @Test
  void getPopulationCaps() {}

  @Test
  void getCurrentPopulation() {}

  @Test
  void getFreePopulations() {}

  @Test
  void getBoosts() {}

  @Test
  void getEffectsFromEntities() {}

  @Test
  void getAllEffectsFromEntities() {}

  @Test
  void getAvailablePopulations() {}

  @Test
  void getAvailableOccupations() {}

  @Test
  void getAvailableTechs() {}
}
