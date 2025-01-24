package org.saucistophe.increledger.model.actions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.saucistophe.increledger.model.Game;

@NoArgsConstructor
@AllArgsConstructor
public class PassTheTime implements Action {

  private long millisElapsed;

  @Override
  public boolean isValid(Game game) {
    return millisElapsed > 0;
  }

  @Override
  public void execute(Game game) {
    for (var entry : game.getOccupations().entrySet()) {
      var occupation = entry.getKey();
      var numberOfAssignees = entry.getValue();

      for (var producedResource : occupation.resourcesProduced.entrySet()) {
        var resource = producedResource.getKey();
        var amount = producedResource.getValue();

        var existingAmount = game.getResources().getOrDefault(resource, 0.);
        var newAmount = existingAmount + amount * numberOfAssignees * millisElapsed / 1000.;
        game.getResources().put(resource, newAmount);
        // TODO handle storage here
      }
    }
  }
}
