package org.saucistophe.increledger.model.resources;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.saucistophe.increledger.model.resources.Resource;

class ResourceTest {

  @Test
  void allSubClassesAreRegistered() throws JsonProcessingException {
    Reflections reflections = new Reflections(Resource.class.getPackageName());
    var subclasses = reflections.getSubTypesOf(Resource.class);
    assertNotEquals(0, subclasses.size());

    for (var subClass : subclasses) {
      var jsonString =
        " {\"type\": \"insertTypeHere\", \"amount\" : 12}";
      jsonString = jsonString.replace("insertTypeHere", subClass.getSimpleName());
      var occupation = new ObjectMapper().readValue(jsonString, Resource.class);
      assertNotNull(occupation);
      assertEquals(subClass, occupation.getClass());
    }
  }
}
