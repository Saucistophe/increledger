package org.saucistophe.increledger.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

@Singleton
public class JsonConfiguration implements ObjectMapperCustomizer {

  public void customize(ObjectMapper mapper) {
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }
}
