package org.saucistophe.increledger.logic;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.actions.AssignOccupation;
import org.saucistophe.increledger.model.actions.Research;
import org.saucistophe.increledger.model.actions.RespondToDialog;
import org.saucistophe.increledger.model.actions.UnassignOccupation;
import org.saucistophe.increledger.model.rules.GameRules;

@ApplicationScoped
@RequiredArgsConstructor
public class ActionsVisitor {

  private final GameRules gameRules;
  private final GameComputingService gameService;
  private final OneTimeEffectVisitor oneTimeEffectVisitor;

  public boolean isValid(Research research, Game game) {
    var techName = research.getTech();
    if (techName == null) {
      Log.info("No tech provided");
      return false;
    }

    if (!game.getTechs().containsKey(techName)) {
      Log.info("Tech not known in current game");
      return false;
    }

    // TODO handle cap
    var tech = gameRules.getTechById(techName);
    if (tech == null) {
      Log.info("Tech " + techName + " not found in the game's rules");
      return false;
    }

    // TODO handle exponential cost
    if (!game.hasResources(tech.getCost())) {
      Log.info("Not enough resources");
      return false;
    }
    return true;
  }

  public void execute(Research research, Game game) {
    var techName = research.getTech();
    var tech = gameRules.getTechById(techName);
    if (tech == null)
      throw new InternalServerErrorException(
          "Tech "
              + techName
              + " not found in the game's rules, should have been caught in action's validation");
    game.spendResources(tech.getCost());
    game.getTechs().compute(techName, (t, amount) -> amount == null ? 1 : amount + 1);
  }

  public boolean isValid(AssignOccupation assignOccupation, Game game) {
    var occupationId = assignOccupation.getOccupation();
    var numbersOfAssignees = assignOccupation.getNumbersOfAssignees();

    if (occupationId == null) {
      Log.error("Occupation is null in assignment");
      return false;
    }

    if (!game.getOccupations().containsKey(occupationId)) {
      Log.error("Occupation " + occupationId + " not found in game's occupations");
      return false;
    }

    var occupation = gameRules.getOccupationById(occupationId);
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
    if (!gameOccupations.containsKey(occupation))
      throw new InternalServerErrorException(
          "Occupation "
              + occupation
              + " not found in game, should have been caught in action's validation");
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

    var currentAssignees = game.getOccupations().getOrDefault(occupationId, 0L);
    if (numbersOfAssignees <= 0 || currentAssignees < numbersOfAssignees) {
      Log.error("Invalid number for assigment: " + numbersOfAssignees);
      return false;
    }

    var occupation = gameRules.getOccupationById(occupationId);
    if (occupation == null) {
      Log.error("Occupation " + occupationId + " not found in game's rules");
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

  public boolean isValid(RespondToDialog respondToDialog, Game game) {

    // You can only respond to the first dialog that popped.
    if (game.getDialogs().isEmpty()
        || !game.getDialogs().getFirst().equals(respondToDialog.getDialog())) return false;

    var dialog = gameRules.getDialogById(respondToDialog.getDialog());
    if (dialog == null) return false;
    var choice = dialog.getDialogChoiceByName(respondToDialog.getChoice());
    return choice != null;
  }

  public void execute(RespondToDialog respondToDialog, Game game) {
    var dialog = gameRules.getDialogById(respondToDialog.getDialog());
    var choice = dialog.getDialogChoiceByName(respondToDialog.getChoice());
    choice.getEffects().forEach(e -> e.acceptExecution(oneTimeEffectVisitor, game));
    game.getDialogs().remove(respondToDialog.getDialog());
  }
}
