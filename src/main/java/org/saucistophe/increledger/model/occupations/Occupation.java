package org.saucistophe.increledger.model.occupations;

import static org.saucistophe.increledger.model.resources.Resource.*;

import com.fasterxml.jackson.databind.EnumNamingStrategies;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.saucistophe.increledger.model.resources.Resource;

@EnumNaming(EnumNamingStrategies.CamelCaseStrategy.class)
@AllArgsConstructor
public enum Occupation {
  HUNTER(Map.of(FOOD, 1.)),
  GATHERER(Map.of(FOOD, 0.5, KNOWLEDGE, 0.1)),
  WOOD_CUTTER(Map.of(WOOD, 1.));

  public final Map<Resource, Double> resourcesProduced;
}
