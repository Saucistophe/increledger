package org.saucistophe.increledger.model.actions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.saucistophe.increledger.model.Game;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = AssignOccupation.class, name = "Assign")})
public interface Action {
  boolean isValid(Game game);

  void execute(Game game);
}
