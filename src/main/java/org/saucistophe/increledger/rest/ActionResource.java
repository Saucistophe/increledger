package org.saucistophe.increledger.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import lombok.AllArgsConstructor;
import org.saucistophe.increledger.logic.GameService;
import org.saucistophe.increledger.model.actions.Action;

@Path("/action")
@AllArgsConstructor
public class ActionResource {

  GameService gameService;

  @GET
  @Path("/samples")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Action> getSamples() {
    // TODO
    // return List.of(new AssignOccupation(, 1L), new Research(new Hatchet()));
    return List.of();
  }
}
