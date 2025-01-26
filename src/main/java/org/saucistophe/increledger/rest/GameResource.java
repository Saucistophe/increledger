package org.saucistophe.increledger.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.saucistophe.increledger.logic.GameService;
import org.saucistophe.increledger.model.dto.GameDTO;

@Path("/game")
@AllArgsConstructor
public class GameResource {

  GameService gameService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public GameDTO getNewGame() {
    return gameService.newGame();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public GameDTO processGame(GameDTO game) {
    return gameService.process(game);
  }

  @POST
  @Path("/production")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Double> getProduction(GameDTO gameDto) {
    return gameService.getCurrentProduction(gameDto.getGame());
  }
}
