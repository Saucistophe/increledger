package org.saucistophe.increledger.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import org.saucistophe.increledger.model.actions.Action;

@Data
public class GameDTO {

  private Game game;
  private String signature;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<Action> actions = null;
}
