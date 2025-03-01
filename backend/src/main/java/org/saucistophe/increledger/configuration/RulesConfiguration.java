package org.saucistophe.increledger.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.configuration.ConfigurationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Produces;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.saucistophe.increledger.model.rules.GameRules;
import org.saucistophe.increledger.model.rules.NamedEntity;

@AllArgsConstructor
public class RulesConfiguration {

  @ConfigProperty(name = "rules.path")
  String rulesPath;

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

    ensureAllTargetsAreFound(gameRules);
    // TODO look for cycles in tech boosts

    return gameRules;
  }

  private void ensureAllTargetsAreFound(GameRules gameRules) {

    var entitiesInError =
        Stream.<List<? extends NamedEntity>>of(
                gameRules.getTechs(),
                gameRules.getResources(),
                gameRules.getPopulations(),
                gameRules.getResources())
            .flatMap(Collection::stream)
            .filter(e -> !e.isValid(gameRules))
            .toList();
    for (var e : entitiesInError) Log.error("Rules not valid for: " + e);
    if (!entitiesInError.isEmpty()) {
      throw new ConfigurationException(
          "Could not load rules because of " + entitiesInError.size() + " errors");
    }
  }
}
