package org.saucistophe.increledger.model.rules;

import jakarta.validation.constraints.Min;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Tech extends NamedEntityWithEffects {

  private Map<String, Double> cost;

  // TODO handle counts and caps
  @Min(-1)
  private long initialCap = -1; // A cap of -1 means unlimited uses; ok for buildings.
}
