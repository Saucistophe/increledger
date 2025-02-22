package org.saucistophe.increledger.model.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.logging.Log;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.InternalServerErrorException;
import java.util.List;
import lombok.Data;

@RegisterForReflection
@Data
public class GameRules {

  @NotEmpty @Valid private List<Resource> resources;
  @NotEmpty @Valid private List<Population> populations;
  @NotEmpty @Valid private List<Occupation> occupations;
  @NotEmpty @Valid private List<Tech> techs;

  private static InternalServerErrorException logIdNotFound(String target, String id) {
    Log.info(target + " with id " + id + " not found");
    return new InternalServerErrorException("Resource with id " + id + " not found");
  }

  @JsonIgnore
  public Resource getResourceById(String id) {
    var resourceOptional = resources.stream().filter(o -> o.getName().equals(id)).findFirst();

    if (resourceOptional.isEmpty()) {
      throw logIdNotFound("Resource", id);
    } else {
      return resourceOptional.get();
    }
  }

  @JsonIgnore
  public Population getPopulationById(String id) {
    var populationOptional = populations.stream().filter(o -> o.getName().equals(id)).findFirst();

    if (populationOptional.isEmpty()) {
      throw logIdNotFound("Population", id);
    } else {
      return populationOptional.get();
    }
  }

  @JsonIgnore
  public Occupation getOccupationById(String id) {
    var occupationOptional = occupations.stream().filter(o -> o.getName().equals(id)).findFirst();

    if (occupationOptional.isEmpty()) {
      throw logIdNotFound("Occupation", id);
    } else {
      return occupationOptional.get();
    }
  }

  @JsonIgnore
  public Tech getTechById(String id) {
    var techOptional = techs.stream().filter(o -> o.getName().equals(id)).findFirst();

    if (techOptional.isEmpty()) {
      throw logIdNotFound("Tech", id);
    } else {
      return techOptional.get();
    }
  }
}
