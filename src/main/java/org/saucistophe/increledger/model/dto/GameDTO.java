package org.saucistophe.increledger.model.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.actions.Action;

@Data
public class GameDTO {

  private Game game;
  private String signature;

  private List<Action> actions = new ArrayList<>();
}
