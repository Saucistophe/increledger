package org.saucistophe.increledger.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.junit.QuarkusTest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.saucistophe.increledger.model.GameDto;
import org.saucistophe.increledger.model.actions.AssignOccupation;

@QuarkusTest
class GameResourceTest {

  @Test
  void getNewGame() {
    var gameDto = given().when().get("/game").then().statusCode(200).extract().as(GameDto.class);

    assertEquals("Dummy Signature", gameDto.getSignature());
    assertEquals(List.of(), gameDto.getActions());
    assertEquals(2, gameDto.getGame().getPopulations().get("people"));
    assertTrue(gameDto.getGame().getTimestamp() > 1738364824582L);
  }

  @Test
  void processGame() {
    var gameDto = given().when().get("/game").then().statusCode(200).extract().as(GameDto.class);
    gameDto.getActions().add(new AssignOccupation("woodcutter", 1L));
    gameDto =
        given()
            .header("Content-type", "application/json")
            .and()
            .body(gameDto)
            .when()
            .post("/game")
            .then()
            .statusCode(200)
            .extract()
            .as(GameDto.class);

    // Production not yet started
    assertEquals(0, gameDto.getGame().getResources().get("wood"));

    CompletableFuture.runAsync(
            () -> {}, CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS))
        .join();
    gameDto =
        given()
            .header("Content-type", "application/json")
            .and()
            .body(gameDto)
            .when()
            .post("/game")
            .then()
            .statusCode(200)
            .extract()
            .as(GameDto.class);
    var currentWood = gameDto.getGame().getResources().get("wood");
    assertTrue(currentWood > 0.);
  }
}
