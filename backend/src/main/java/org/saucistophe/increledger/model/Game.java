package org.saucistophe.increledger.model;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;
import lombok.Data;

@Data
public class Game {

  private Map<String, Long> populations = new HashMap<>();
  private Map<String, Long> occupations = new HashMap<>();
  private Map<String, Long> techs = new HashMap<>();
  private Map<String, Double> resources = new HashMap<>();
  private List<String> dialogs = new ArrayList<>();
  private List<String> messages = new ArrayList<>();
  private List<String> flags = new ArrayList<>();

  @NotNull private Long timestamp = Instant.now().toEpochMilli();

  public void updateTimestamp() {
    timestamp = Instant.now().toEpochMilli();
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

  public boolean hasAny(Map<String, Double> requirements) {
    if (requirements == null) return true;

    Stream<? extends Map.Entry<String, ? extends Number>> allEntities =
        Stream.of(populations, occupations, techs, resources).flatMap(m -> m.entrySet().stream());

    var satisfiedRequirements =
        allEntities
            .filter(
                e ->
                    requirements.containsKey(e.getKey())
                        && e.getValue().doubleValue() >= requirements.get(e.getKey()))
            .count();

    return satisfiedRequirements >= requirements.size();
  }
}
