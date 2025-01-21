package org.saucistophe.increledger.model.occupations;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.saucistophe.increledger.model.occupations.Occupation;

class OccupationTest {

  @Test
  void allSubClassesAreRegistered() throws JsonProcessingException {
    Reflections reflections = new Reflections(Occupation.class.getPackageName());
    var subclasses = reflections.getSubTypesOf(Occupation.class);
    assertNotEquals(0, subclasses.size());

    for (var subClass : subclasses) {
      var jsonString =
          " {\"type\": \"insertTypeHere\", \"numbersOfAssignees\" : 12}";
      jsonString = jsonString.replace("insertTypeHere", subClass.getSimpleName());
      var occupation = new ObjectMapper().readValue(jsonString, Occupation.class);
      assertNotNull(occupation);
      assertEquals(subClass, occupation.getClass());
    }
  }
}
