package org.saucistophe.increledger.model.effects;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Unlock implements Effect {

  @NotBlank String target;
}
