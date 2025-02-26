package org.saucistophe.increledger.model.rules.effects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.saucistophe.increledger.model.rules.GameRules;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
public interface Effect {

  boolean isValid(GameRules gameRules);
}
