package org.saucistophe.increledger.model.tech;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.saucistophe.increledger.model.resources.Resource;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Hatchet.class, name = "Hatchet"),
})
@SuperBuilder
@NoArgsConstructor
@Data
public abstract class Tech {
  public abstract Map<Resource, Long> requirements();
}
