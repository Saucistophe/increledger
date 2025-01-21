package org.saucistophe.incremental.resources;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.saucistophe.incremental.occupations.Woodcutter;

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
