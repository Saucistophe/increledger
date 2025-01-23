package org.saucistophe.increledger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.saucistophe.increledger.model.occupations.Occupation;
import org.saucistophe.increledger.model.resources.Resource;

@Data
public class Game {

  private long maxPopulation = 5;
  private long population = 1;

  private Map<Occupation, Long> occupations = new HashMap<>();
  private Map<Resource, Double> resources = new HashMap<>();

  private Long timestamp = Instant.now().toEpochMilli();

  public void updateTimestamp() {
    timestamp = Instant.now().toEpochMilli();
  }

  @JsonIgnore
  public long getFreePopulation() {
    return population - occupations.values().stream().mapToLong(Long::longValue).sum();
  }

  @JsonIgnore
  public String toJson() {
    // TODO move this to other package
    try {
      return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static Game fromJson(String json) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json, Game.class);
  }
}
