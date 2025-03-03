package org.saucistophe.increledger.model.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.logging.Log;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@RegisterForReflection
@EqualsAndHashCode(callSuper = true)
@Data
public class Dialog extends NamedEntity {
  // Todo add translations
  private @NotEmpty String text;
  private @NotEmpty @Valid List<DialogChoice> choices;

  @JsonIgnore
  public DialogChoice getDialogChoiceByName(String name) {
    var choiceOptional = choices.stream().filter(d -> d.getName().equals(name)).findFirst();

    if (choiceOptional.isEmpty()) {
      Log.info("Dialog choice with id " + name + " not found");
      return null;
    } else {
      return choiceOptional.get();
    }
  }

  @Override
  public boolean isValid(GameRules gameRules) {
    return choices.stream().allMatch(d -> d.isValid(gameRules));
  }
}
