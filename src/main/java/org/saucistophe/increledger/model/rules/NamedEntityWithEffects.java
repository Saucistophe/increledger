package org.saucistophe.increledger.model.rules;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.saucistophe.increledger.model.effects.Effect;

@EqualsAndHashCode(callSuper = true)
@Data
public class NamedEntityWithEffects extends NamedEntity{
  @Valid private List<Effect> effects;
  boolean unlocked = false;
}
