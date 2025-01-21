package org.saucistophe.incremental.actions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.saucistophe.incremental.Game;
import org.saucistophe.incremental.occupations.Occupation;

@NoArgsConstructor
@AllArgsConstructor
public class AssignOccupation<T extends Occupation> implements Action {

  long numberOfAssignments;
  Class<T> occupation;

  @Override
  public boolean isValid(Game game) {
    if (occupation == null) return false;
    if (numberOfAssignments <1) return false;
    if (numberOfAssignments > game.getFreePopulation()) return false;

    return true;
  }

  @Override
  public void execute(Game game) {
//    var occupations = game.getOccupations();
//    occupations.computeIfAbsent(occupation,o -> 0L );
//    occupations.put(occupation, occupations.get(occupation) + numberOfAssignments);
  }
}
