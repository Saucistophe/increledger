package org.saucistophe.increledger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.saucistophe.increledger.model.occupations.Occupation;
import org.saucistophe.increledger.model.resources.Resource;

@Data
public class Game {

  private long population = 1;

  private List<Occupation> occupations = new ArrayList<>();
  private List<Resource> resources = new ArrayList<>();

  private Long timestamp = Instant.now().toEpochMilli();

  public void updateTimestamp() {
    timestamp = Instant.now().toEpochMilli();
  }

  @JsonIgnore
  public long getFreePopulation() {
    return population - occupations.stream().mapToLong(Occupation::getNumbersOfAssignees).sum();
  }

  @JsonIgnore
  public String toJson() throws JsonProcessingException {
    return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
  }

  public static Game fromJson(String json) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json, Game.class);
  }
}
