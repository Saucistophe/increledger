package org.saucistophe.increledger.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.configuration.ConfigurationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.saucistophe.increledger.model.dto.GameRules;

@RequiredArgsConstructor
public class RulesConfiguration {
  private final ObjectMapper objectMapper;

  @ApplicationScoped
  @Produces
  public GameRules loadGameRules() {
    try {
      var rulesJson = Files.readString(Path.of("./rules.json"));
      var result = objectMapper.readValue(rulesJson, GameRules.class);
      return result;
    } catch (IOException e) {
      throw new ConfigurationException("Could not load rules from input file", e);
    }
  }
}
