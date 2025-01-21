package org.saucistophe.increledger.model.occupations;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Woodcutter.class, name = "Woodcutter"),
  @JsonSubTypes.Type(value = Gatherer.class, name = "Gatherer")
})
@SuperBuilder
@NoArgsConstructor
@Data
public abstract class Occupation {
  protected long numbersOfAssignees;
}
