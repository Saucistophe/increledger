package org.saucistophe.increledger.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import lombok.AllArgsConstructor;
import org.saucistophe.increledger.logic.GameService;
import org.saucistophe.increledger.model.actions.Action;
import org.saucistophe.increledger.model.actions.AssignOccupation;
import org.saucistophe.increledger.model.actions.Research;
import org.saucistophe.increledger.model.occupations.Occupation;
import org.saucistophe.increledger.model.tech.Hatchet;

@Path("/action")
@AllArgsConstructor
public class ActionResource {

  GameService gameService;

  @GET
  @Path("/samples")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Action> getSamples() {
    return List.of(new AssignOccupation(Occupation.WOOD_CUTTER, 1L), new Research(new Hatchet()));
  }
}
