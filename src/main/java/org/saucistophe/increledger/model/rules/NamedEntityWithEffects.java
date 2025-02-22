package org.saucistophe.increledger.model.rules;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.saucistophe.increledger.model.rules.effects.Effect;

@RegisterForReflection
@EqualsAndHashCode(callSuper = true)
@Data
public class NamedEntityWithEffects extends NamedEntity {
  @Valid private List<Effect> effects;
  boolean unlocked = false;
}
