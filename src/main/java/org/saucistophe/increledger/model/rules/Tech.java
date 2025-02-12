package org.saucistophe.increledger.model.rules;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.saucistophe.increledger.model.effects.Effect;

@EqualsAndHashCode(callSuper = true)
@Data
public class Tech extends NamedEntityWithEffects {

  private Map<String, Double> cost;
  private List<String> prerequisiteTechs;
}
