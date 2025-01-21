package org.saucistophe.increledger.model.resources;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Stone.class, name = "Stone"),
  @JsonSubTypes.Type(value = Wood.class, name = "Wood")
})
@SuperBuilder
@NoArgsConstructor
@Data
public abstract class Resource {
  protected long amount;
}
