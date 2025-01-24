package org.saucistophe.increledger.model.resources;

import com.fasterxml.jackson.databind.EnumNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@EnumNaming(EnumNamingStrategies.CamelCaseStrategy.class)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public enum Resource {
  FOOD,
  KNOWLEDGE,
  STONE,
  WOOD
}
