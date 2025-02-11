package org.saucistophe.increledger.model.effects;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UnlockOccupation implements Effect {

  @NotBlank String occupation;
}
