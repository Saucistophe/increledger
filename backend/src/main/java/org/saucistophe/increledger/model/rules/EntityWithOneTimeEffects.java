package org.saucistophe.increledger.model.rules;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.saucistophe.increledger.model.rules.effects.OneTimeEffect;

@RegisterForReflection
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class EntityWithOneTimeEffects extends Entity {

  @Valid protected List<OneTimeEffect> effects = List.of();
}
