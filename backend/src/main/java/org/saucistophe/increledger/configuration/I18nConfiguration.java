package org.saucistophe.increledger.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import java.io.File;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@AllArgsConstructor
public class I18nConfiguration {

  @ConfigProperty(name = "translation.path")
  String translationPath;

  @Startup
  @ApplicationScoped
  @Produces
  public JsonNode loadTranslations() throws IOException {
    var objectMapper = new ObjectMapper(new YAMLFactory());
    return objectMapper.readTree(new File(translationPath));
  }
}
