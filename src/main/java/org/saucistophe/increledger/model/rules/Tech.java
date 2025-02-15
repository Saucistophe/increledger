package org.saucistophe.increledger.model.rules;

import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Tech extends NamedEntityWithEffects {

  private Map<String, Double> cost;
  private List<String> prerequisiteTechs;

  // TODO handle counts and caps
  @Min(-1)
  private long cap = -1; // A cap of -1 means unlimited uses; ok for buildings.
}
