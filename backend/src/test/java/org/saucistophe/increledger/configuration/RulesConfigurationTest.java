package org.saucistophe.increledger.configuration;

import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.saucistophe.increledger.model.rules.effects.RawProduction;

@QuarkusTest
class RulesConfigurationTest {

  @Test
  void loadingRules() {
    var rulesConfiguration = new RulesConfiguration("./src/test/resources/rules/valid.yaml");
    var rules = rulesConfiguration.loadGameRules();

    assertEquals(4, rules.getResources().size());
    assertEquals("wood", rules.getResources().getFirst().getName());
    assertEquals(50, rules.getResources().getFirst().getInitialCap());

    assertEquals(2, rules.getOccupations().size());
    assertEquals("woodcutter", rules.getOccupations().getFirst().getName());
    assertTrue(rules.getOccupations().getFirst().isUnlocked());
    assertEquals(
        new RawProduction("wood", 1.), rules.getOccupations().getFirst().getEffects().getFirst());
  }

  @Test
  void invalidRules() {
    var rulesConfiguration = new RulesConfiguration("./src/test/resources/rules/invalid.yaml");

    try {
      rulesConfiguration.loadGameRules();
      fail("A validation exception should have been thrown");
    } catch (ConstraintViolationException e) {
      assertEquals(13, e.getConstraintViolations().size());
    }
  }
}
