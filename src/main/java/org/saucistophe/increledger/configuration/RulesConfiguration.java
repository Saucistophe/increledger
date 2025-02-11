package org.saucistophe.increledger.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.configuration.ConfigurationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
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
    GameRules gameRules;
    Validator validator;

    try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
      validator = validatorFactory.getValidator();
      gameRules = objectMapper.readValue(new File(rulesPath), GameRules.class);
    } catch (IOException e) {
      throw new ConfigurationException("Could not load rules from input file", e);
    }

    var violations = validator.validate(gameRules);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
    // TODO look for cycles in tech boosts

    return gameRules;
  }
}
