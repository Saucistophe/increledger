package org.saucistophe.increledger.model.rules.effects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.saucistophe.increledger.logic.OneTimeEffectVisitor;
import org.saucistophe.increledger.model.Game;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
public interface OneTimeEffect {
  boolean acceptValidation(OneTimeEffectVisitor visitor);

  void acceptExecution(OneTimeEffectVisitor visitor, Game game);
}
