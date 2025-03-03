package org.saucistophe.increledger.model.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.logging.Log;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@RegisterForReflection
@Data
public class GameRules {

  @NotEmpty @Valid private List<Resource> resources;
  @NotEmpty @Valid private List<Population> populations;
  @NotEmpty @Valid private List<Occupation> occupations;
  @NotEmpty @Valid private List<Tech> techs;
  @Valid private List<Dialog> dialogs;
  @Valid private List<OneTimeTrigger> triggers;

  private static void logIdNotFound(String target, String id) {
    Log.info(target + " with id " + id + " not found");
  }

  @JsonIgnore
  public Resource getResourceById(String id) {
    var resourceOptional = resources.stream().filter(o -> o.getName().equals(id)).findFirst();

    if (resourceOptional.isEmpty()) {
      logIdNotFound("Resource", id);
      return null;
    } else {
      return resourceOptional.get();
    }
  }

  @JsonIgnore
  public Population getPopulationById(String id) {
    var populationOptional = populations.stream().filter(o -> o.getName().equals(id)).findFirst();

    if (populationOptional.isEmpty()) {
      logIdNotFound("Population", id);
      return null;
    } else {
      return populationOptional.get();
    }
  }

  @JsonIgnore
  public Occupation getOccupationById(String id) {
    var occupationOptional = occupations.stream().filter(o -> o.getName().equals(id)).findFirst();

    if (occupationOptional.isEmpty()) {
      logIdNotFound("Occupation", id);
      return null;
    } else {
      return occupationOptional.get();
    }
  }

  @JsonIgnore
  public Tech getTechById(String id) {
    var techOptional = techs.stream().filter(o -> o.getName().equals(id)).findFirst();

    if (techOptional.isEmpty()) {
      logIdNotFound("Tech", id);
      return null;
    } else {
      return techOptional.get();
    }
  }

  @JsonIgnore
  public Dialog getDialogById(@NotBlank String id) {
    var dialogOptional = dialogs.stream().filter(o -> o.getName().equals(id)).findFirst();

    if (dialogOptional.isEmpty()) {
      logIdNotFound("Dialog", id);
      return null;
    } else {
      return dialogOptional.get();
    }
  }
}
