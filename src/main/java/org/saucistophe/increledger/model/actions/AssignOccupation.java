package org.saucistophe.increledger.model.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saucistophe.increledger.model.Game;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssignOccupation implements Action {

  String occupation;
  Long numbersOfAssignees;

  @Override
  public boolean isValid(Game game) {
    if (occupation == null) return false;
    if (numbersOfAssignees < 1) return false;
    return numbersOfAssignees <= game.getFreePopulation();
  }

  @Override
  public void execute(Game game) {
    var gameOccupations = game.getOccupations();
    var amount = gameOccupations.getOrDefault(occupation, 0L);
    gameOccupations.put(occupation, amount + numbersOfAssignees);
  }
}
