package org.saucistophe.increledger.logic;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.actions.AssignOccupation;
import org.saucistophe.increledger.model.actions.Research;
import org.saucistophe.increledger.model.actions.UnassignOccupation;
import org.saucistophe.increledger.model.rules.GameRules;

@ApplicationScoped
@RequiredArgsConstructor
public class ActionsVisitor {

  private final GameRules gameRules;
  private final GameComputingService gameService;

  public boolean isValid(Research research, Game game) {
    var techName = research.getTech();
    if (techName == null) {
      Log.info("No tech provided");
      return false;
    }

    if (game.getTechs().containsKey(techName)) {
      Log.info("Tech already researched");
      return false;
    }

    // TODO handle cap
    var techOptional =
        gameRules.getTechs().stream().filter(t -> t.getName().equals(techName)).findFirst();

    if (techOptional.isEmpty()) {
      Log.info("Tech " + techName + " not found in the game's rules");
      return false;
    }

    if (!game.hasResources(techOptional.get().getCost())) {
      Log.info("Not enough resources");
      return false;
    }
    return true;
  }

  public void execute(Research research, Game game) {
    var techName = research.getTech();
    var techOptional =
        gameRules.getTechs().stream().filter(t -> t.getName().equals(techName)).findFirst();
    if (techOptional.isEmpty())
      throw new InternalServerErrorException("Tech " + techName + " not found in the game's rules");
    game.spendResources(techOptional.get().getCost());
    game.getTechs().compute(techName, (t, cost) -> cost == null ? 1 : cost + 1);
  }

  public boolean isValid(AssignOccupation assignOccupation, Game game) {
    var occupationId = assignOccupation.getOccupation();
    var numbersOfAssignees = assignOccupation.getNumbersOfAssignees();

    if (occupationId == null) {
      Log.error("Occupation is null in assignment");
      return false;
    }
    var occupation = gameRules.getOccupationById(occupationId);
    if (occupation == null) {
      Log.error("Occupation " + occupationId + " not found in game's rules");
      return false;
    }
    var freePopulations = gameService.getFreePopulations(game);
    if (numbersOfAssignees < 1
        || numbersOfAssignees > freePopulations.get(occupation.getPopulation())) {
      Log.error("Invalid number for assigment: " + numbersOfAssignees);
      return false;
    }
    return true;
  }

  public void execute(AssignOccupation assignOccupation, Game game) {
    var occupation = assignOccupation.getOccupation();
    var numbersOfAssignees = assignOccupation.getNumbersOfAssignees();

    var gameOccupations = game.getOccupations();
    var amount = gameOccupations.getOrDefault(occupation, 0L);
    gameOccupations.put(occupation, amount + numbersOfAssignees);
  }

  public boolean isValid(UnassignOccupation unassignOccupation, Game game) {
    var occupationId = unassignOccupation.getOccupation();
    var numbersOfAssignees = unassignOccupation.getNumbersOfAssignees();

    if (occupationId == null) {
      Log.error("Occupation is null in assignment");
      return false;
    }
    var occupation = gameRules.getOccupationById(occupationId);
    if (occupation == null) {
      Log.error("Occupation " + occupationId + " not found in game's rules");
      return false;
    }
    var currentAssignees = game.getOccupations().getOrDefault(occupationId, 0L);

    if (numbersOfAssignees <= 0 || currentAssignees < numbersOfAssignees) {
      Log.error("Invalid number for assigment: " + numbersOfAssignees);
      return false;
    }

    return true;
  }

  public void execute(UnassignOccupation unassignOccupation, Game game) {
    var occupation = unassignOccupation.getOccupation();
    var numbersOfAssignees = unassignOccupation.getNumbersOfAssignees();

    var gameOccupations = game.getOccupations();
    var amount = gameOccupations.getOrDefault(occupation, 0L);
    gameOccupations.put(occupation, amount - numbersOfAssignees);
  }
}
