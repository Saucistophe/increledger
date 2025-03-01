package org.saucistophe.increledger.rest;

import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import org.saucistophe.increledger.logic.GameService;
import org.saucistophe.increledger.model.GameDto;

@Path("/game")
@AllArgsConstructor
public class GameResource {

  GameService gameService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public GameDto getNewGame() {
    return gameService.newGame();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public GameDto processGame(@Valid GameDto game) {
    return gameService.process(game);
  }
}
