package org.saucistophe;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.saucistophe.increledger.logic.CryptoService;
import org.saucistophe.increledger.logic.GameService;
import org.saucistophe.increledger.model.GameDTO;

public class Main {

  public static void main(String[] args) throws IOException {
    if (args.length > 0 && args[0].equals("generate")) {
      var crypto = new CryptoService();
      crypto.generateRSAKeyPair();
      crypto.storeKeys();
      System.exit(0);
    }
    var objectWriter = new ObjectMapper();
    GameDTO gameDto = null;
    var gameService = new GameService();

    if (!Files.exists(Path.of("in.json"))) {
      gameDto = gameService.newGame();
    } else {
      Files.readString(Path.of("in.json"));
      gameDto = objectWriter.readValue(Files.readString(Path.of("in.json")), GameDTO.class);
    }

    // for (int i = 0; i < 100; i++) gameDto = gameService.process(gameDto);

    gameDto = gameService.process(gameDto);

    if (gameDto != null)
      Files.writeString(
          Path.of("in.json"),
          objectWriter.writerWithDefaultPrettyPrinter().writeValueAsString(gameDto));
  }
}
