package org.saucistophe.increledger.model.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.logging.Log;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.InternalServerErrorException;
import java.util.List;
import lombok.Data;

@Data
public class GameRules {

  @NotEmpty @Valid private List<Resource> resources;
  @Valid private List<Occupation> occupations;
  @Valid private List<Tech> techs;

  @JsonIgnore
  public Occupation getOccupationById(String id) {
    var occupationOptional = occupations.stream().filter(o -> o.getName().equals(id)).findFirst();

    if (occupationOptional.isEmpty()) {
      Log.error("Occupation with id " + id + " not found");
      throw new InternalServerErrorException("Occupation with id " + id + " not found");
    } else {
      return occupationOptional.get();
    }
  }
}
