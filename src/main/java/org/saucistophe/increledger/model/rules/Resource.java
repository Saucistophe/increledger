package org.saucistophe.increledger.model.rules;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Resource extends NamedEntity {
  @DecimalMin("1")
  private double initialCap;
}
