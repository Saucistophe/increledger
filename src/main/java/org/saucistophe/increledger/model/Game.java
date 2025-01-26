package org.saucistophe.increledger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.*;
import lombok.Data;

@Data
public class Game {

  private long maxPopulation = 5;
  private long population = 2;

  private Map<String, Long> occupations = new HashMap<>();
  private Map<String, Double> resources = new HashMap<>();
  // private List<Tech> techs = new ArrayList<>();

  private Long timestamp = Instant.now().toEpochMilli();

  public void updateTimestamp() {
    timestamp = Instant.now().toEpochMilli();
  }

  @JsonIgnore
  public long getFreePopulation() {
    return population - occupations.values().stream().mapToLong(Long::longValue).sum();
  }

  public void spendResources(Map<String, Long> resourcesToSpend) {
    for (var entry : resourcesToSpend.entrySet()) {
      resources.put(entry.getKey(), resources.get(entry.getKey()) - entry.getValue());
    }
  }

  public boolean hasResources(Map<String, Long> requirements) {
    for (var entry : requirements.entrySet()) {
      if (!resources.containsKey(entry.getKey())) return false;
      if (resources.get(entry.getKey()) < entry.getValue()) return false;
    }
    return true;
  }
}
