package org.saucistophe.increledger.model.occupations;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.saucistophe.increledger.model.resources.Resource;

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

  public abstract List<Resource> produces();
}
