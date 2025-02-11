package org.saucistophe.increledger.model.rules;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.saucistophe.increledger.model.effects.Effect;

@EqualsAndHashCode(callSuper = true)
@Data
public class Occupation extends NamedEntityWithEffects{
  @NotBlank private String population;
  private boolean unlocked = false;
}
