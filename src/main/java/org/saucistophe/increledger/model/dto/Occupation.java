package org.saucistophe.increledger.model.dto;

import java.util.Map;
import lombok.Data;

@Data
public class Occupation {

  private String name;
  private Map<String, Double> resourcesProduced;
}
