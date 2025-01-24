package org.saucistophe.increledger.model.tech;

import static org.saucistophe.increledger.model.resources.Resource.*;

import java.util.Map;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.saucistophe.increledger.model.resources.Resource;

@SuperBuilder
@NoArgsConstructor
public class Hatchet extends Tech {
  @Override
  public Map<Resource, Long> requirements() {
    return Map.of(WOOD, 10L, KNOWLEDGE, 10L);
  }
}
