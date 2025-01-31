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
import org.saucistophe.increledger.model.rules.GameRules;

public class RulesConfiguration {
  @Startup
  @ApplicationScoped
  @Produces
  public GameRules loadGameRules() {
    var objectMapper = new ObjectMapper(new YAMLFactory());
    try {
      Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
      var gameRules = objectMapper.readValue(new File("./rules.yaml"), GameRules.class);
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
