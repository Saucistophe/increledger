package org.saucistophe.increledger.model.rules;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.saucistophe.increledger.model.effects.Effect;

@Data
public class Tech {

  @NotBlank private String name;
  private Map<String, Double> cost;
  @Valid private List<Effect> effects;
  private List<String> prerequisiteTechs;
}
