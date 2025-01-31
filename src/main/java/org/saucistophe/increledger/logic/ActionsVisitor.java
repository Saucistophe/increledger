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
  private final GameService gameService;

  public boolean isValid(Research research, Game game) {
    var tech = research.getTech();
    if (tech == null) {
      Log.info("No tech provided");
      return false;
    }
    // TODO
    if (false) {
      Log.info("Tech already researched");
      return false;
    }
    // TODO
    if (false) {
      Log.info("Not enough resources");
      return false;
    }
    return true;
  }

  public void execute(Research research, Game game) {
    // TODO
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
