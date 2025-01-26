package org.saucistophe.increledger.model.actions;

import io.quarkus.logging.Log;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saucistophe.increledger.model.Game;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Research implements Action {

  String tech;

  @Override
  public boolean isValid(Game game) {
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

  @Override
  public void execute(Game game) {
    // TODO
    // game.spendResources(tech.requirements());
    // game.getTechs().add(tech);
  }
}
