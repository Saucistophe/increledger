package org.saucistophe.increledger.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import java.util.List;

@ApplicationScoped
public class GameRules {
  List<String> resources;
  List<Occupation> occupations;

  public GameRules() {}

  @JsonIgnore
  public Occupation getOccupationById(String id) {
    return occupations.stream()
        .filter(o -> o.getName().equals(id))
        .findFirst()
        .orElseThrow(
            () -> new InternalServerErrorException("Could not find occupation with id: " + id));
  }
}
