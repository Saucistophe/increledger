package org.saucistophe.increledger.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.saucistophe.increledger.model.actions.Action;

@Data
public class GameDto {

  public record SettingsDto(List<String> supportedLanguages, String selectedLanguage) {}

  @Valid private Game game;
  @NotBlank private String signature;
  SettingsDto settings = new SettingsDto(List.of(), null);

  private List<Action> actions = new ArrayList<>();

  private GameDescription gameDescription;
}
