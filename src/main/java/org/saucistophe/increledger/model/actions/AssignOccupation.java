package org.saucistophe.increledger.model.actions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.occupations.Occupation;

@NoArgsConstructor
@AllArgsConstructor
public class AssignOccupation implements Action {

  Occupation occupation;

  @Override
  public boolean isValid(Game game) {
    if (occupation == null) return false;
    if (occupation.getNumbersOfAssignees() < 1) return false;
    return occupation.getNumbersOfAssignees() <= game.getFreePopulation();
  }

  @Override
  public void execute(Game game) {
    var gameOccupations = game.getOccupations();
    var existingOccupation =
        gameOccupations.stream()
            .filter(o -> o.getClass().equals(occupation.getClass()))
            .findFirst();
    if (existingOccupation.isEmpty()) {
      gameOccupations.add(occupation);
    } else
      existingOccupation
          .get()
          .setNumbersOfAssignees(
              existingOccupation.get().getNumbersOfAssignees()
                  + occupation.getNumbersOfAssignees());
  }
}
