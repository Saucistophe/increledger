package org.saucistophe.increledger.logic;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.actions.AssignOccupation;
import org.saucistophe.increledger.model.actions.Research;
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

    game.spendResources(techOptional.get().getCost());
  }

  public boolean isValid(AssignOccupation assignOccupation, Game game) {
    var occupation = assignOccupation.getOccupation();
    var numbersOfAssignees = assignOccupation.getNumbersOfAssignees();

    if (occupation == null) {
      Log.error("Occupation is null in assignment");
      return false;
    }
    if (gameRules.getOccupationById(occupation) == null) {
      Log.error("Occupation " + occupation + " not found in game's rules");
      return false;
    }
    if (numbersOfAssignees < 1 || numbersOfAssignees > game.getFreePopulation()) {
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
}
