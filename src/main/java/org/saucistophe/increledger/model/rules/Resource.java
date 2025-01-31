package org.saucistophe.increledger.model.rules;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Resource {
  @NotBlank private String name;

  @DecimalMin("1")
  private double initialCap;
}
