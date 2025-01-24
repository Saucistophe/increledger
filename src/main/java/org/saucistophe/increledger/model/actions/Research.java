package org.saucistophe.increledger.model.actions;

import io.quarkus.logging.Log;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.tech.Tech;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Research implements Action {

  Tech tech;

  @Override
  public boolean isValid(Game game) {
    if (tech == null) {
      Log.info("No tech provided");
      return false;
    }
    if (game.getTechs().stream().anyMatch(t -> t.getClass().isAssignableFrom(tech.getClass()))) {
      Log.info("Tech already researched");
      return false;
    }
    if (!game.hasResources(tech.requirements())) {
      Log.info("Not enough resources");
      return false;
    }
    return true;
  }

  @Override
  public void execute(Game game) {
    game.spendResources(tech.requirements());
    game.getTechs().add(tech);
  }
}
