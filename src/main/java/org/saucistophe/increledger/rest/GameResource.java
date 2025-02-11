package org.saucistophe.increledger.rest;

import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.saucistophe.increledger.logic.GameComputingService;
import org.saucistophe.increledger.model.GameDto;

@Path("/game")
@AllArgsConstructor
public class GameResource {

  GameComputingService gameService;

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

  @POST
  @Path("/production")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Double> getProduction(@Valid GameDto gameDto) {
    return gameService.getCurrentProduction(gameDto.getGame());
  }

  @POST
  @Path("/occupations")
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> getAvailableOccupations(@Valid GameDto gameDto) {
    return gameService.getAvailableOccupations(gameDto.getGame());
  }
}
