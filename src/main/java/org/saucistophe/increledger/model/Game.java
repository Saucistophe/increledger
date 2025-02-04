package org.saucistophe.increledger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.*;
import lombok.Data;

@Data
public class Game {

  @Min(1)
  private long maxPopulation = 5;

  @Min(0)
  private long population = 2;

  private Map<String, Long> occupations = new HashMap<>();
  private Map<String, Double> resources = new HashMap<>();
  private List<String> techs = new ArrayList<>();

  @NotNull private Long timestamp = Instant.now().toEpochMilli();

  public void updateTimestamp() {
    timestamp = Instant.now().toEpochMilli();
  }

  @JsonIgnore
  public long getFreePopulation() {
    return population - occupations.values().stream().mapToLong(Long::longValue).sum();
  }

  public void spendResources(Map<String, Double> resourcesToSpend) {
    for (var entry : resourcesToSpend.entrySet()) {
      resources.put(entry.getKey(), resources.get(entry.getKey()) - entry.getValue());
    }
  }

  public boolean hasResources(Map<String, Double> requirements) {
    for (var entry : requirements.entrySet()) {
      if (!resources.containsKey(entry.getKey())) return false;
      if (resources.get(entry.getKey()) < entry.getValue()) return false;
    }
    return true;
  }
}
