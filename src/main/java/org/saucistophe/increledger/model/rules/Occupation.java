package org.saucistophe.increledger.model.rules;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.Data;

@Data
public class Occupation {

  @NotBlank private String name;
  private boolean unlocked = false;
  private Map<String, Double> resourcesProduced;
}
