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
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.saucistophe.increledger.model.rules.GameRules;

@RequiredArgsConstructor
public class RulesConfiguration {

  @ConfigProperty(name = "rules.path")
  final String rulesPath;

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
    // TODO ensure nothing boosts the caps of something with cap -1

    return gameRules;
  }
}
