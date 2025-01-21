package org.saucistophe.increledger.model.actions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.occupations.Occupation;

@NoArgsConstructor
@AllArgsConstructor
public class PassTheTime implements Action {

  private long millisElapsed;

  @Override
  public boolean isValid(Game game) {
    return true;
  }

  @Override
  public void execute(Game game) {
    for (Occupation occupation : game.getOccupations()) {
      var productedResources = occupation.produces();
      for (var productedResource : productedResources) {
        var currentResource = game.getResource(productedResource.getClass());
        if (currentResource == null) {
          // MEH game.getResources().add(productedResource);
        }
      }
    }
  }
}
