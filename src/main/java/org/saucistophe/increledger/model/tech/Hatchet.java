package org.saucistophe.increledger.model.tech;

import java.util.List;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.saucistophe.increledger.model.resources.Resource;
import org.saucistophe.increledger.model.resources.Stone;
import org.saucistophe.increledger.model.resources.Wood;

@SuperBuilder
@NoArgsConstructor
public class Hatchet extends Tech {
  @Override
  List<Resource> requirements() {
    return List.of(Wood.builder().amount(10).build(), Stone.builder().amount(3).build());
  }
}
