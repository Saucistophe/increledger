package org.saucistophe.increledger.model.effects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.saucistophe.increledger.model.Game;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
public interface Effect {
  void execute(Game game);
}
