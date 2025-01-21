package org.saucistophe.increledger.model.occupations;

import java.util.List;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.saucistophe.increledger.model.resources.Knowledge;
import org.saucistophe.increledger.model.resources.Resource;
import org.saucistophe.increledger.model.resources.Wood;

@SuperBuilder
@NoArgsConstructor
public class Woodcutter extends Occupation {
  @Override
  public List<Resource> produces() {
    return List.of(Wood.builder().amount(1).build(), Knowledge.builder().amount(0.01).build());
  }
}
