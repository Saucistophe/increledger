package org.saucistophe.increledger.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.configuration.ConfigurationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.ws.rs.Produces;
import java.io.File;
import java.io.IOException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.saucistophe.increledger.model.rules.GameRules;

public class RulesConfiguration {

  String rulesPath;
  public RulesConfiguration(@ConfigProperty(name = "rules.path") String rulesPath) {
    this .rulesPath = rulesPath;
  }

  @Startup
  @ApplicationScoped
  @Produces
  public GameRules loadGameRules() {
    var objectMapper = new ObjectMapper(new YAMLFactory());
    try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
      var validator = validatorFactory.getValidator();
      var gameRules = objectMapper.readValue(new File(rulesPath), GameRules.class);
      var violations = validator.validate(gameRules);
      if (!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
      }
      return gameRules;
    } catch (IOException e) {
      throw new ConfigurationException("Could not load rules from input file", e);
    }
  }
}
