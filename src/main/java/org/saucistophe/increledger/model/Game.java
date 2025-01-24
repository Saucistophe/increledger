package org.saucistophe.increledger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.*;
import lombok.Data;
import org.saucistophe.increledger.model.occupations.Occupation;
import org.saucistophe.increledger.model.resources.Resource;
import org.saucistophe.increledger.model.tech.Tech;

@Data
public class Game {

  private long maxPopulation = 5;
  private long population = 2;

  private Map<Occupation, Long> occupations = new EnumMap<>(Occupation.class);
  private Map<Resource, Double> resources = new EnumMap<>(Resource.class);
  private List<Tech> techs = new ArrayList<>();

  private Long timestamp = Instant.now().toEpochMilli();

  public void updateTimestamp() {
    timestamp = Instant.now().toEpochMilli();
  }

  @JsonIgnore
  public long getFreePopulation() {
    return population - occupations.values().stream().mapToLong(Long::longValue).sum();
  }

  public void spendResources(Map<Resource, Long> resourcesToSpend) {
    for (var entry : resourcesToSpend.entrySet()) {
      resources.put(entry.getKey(), resources.get(entry.getKey()) - entry.getValue());
    }
  }

  public boolean hasResources(Map<Resource, Long> requirements) {
    for (var entry : requirements.entrySet()) {
      if (!resources.containsKey(entry.getKey())) return false;
      if (resources.get(entry.getKey()) < entry.getValue()) return false;
    }
    return true;
  }
}
