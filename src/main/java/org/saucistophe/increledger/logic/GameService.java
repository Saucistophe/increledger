package org.saucistophe.increledger.logic;

import java.security.*;
import java.util.ArrayList;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.GameDTO;
import org.saucistophe.increledger.model.actions.PassTheTime;

public class GameService {

  private CryptoService cryptoService = new CryptoService();

  public GameDTO newGame() {
    var game = new Game();
    var resultGameDto = new GameDTO();
    resultGameDto.setGame(game);
    resultGameDto.setSignature(cryptoService.sign(game.toJson()));

    return resultGameDto;
  }

  public GameDTO process(GameDTO gameDto) {
    // Check signature
    var verification = cryptoService.verify(gameDto.getGame().toJson(), gameDto.getSignature());
    if (!verification) {
      System.err.println("Invalid signature");
      return null;
    }

    var game = gameDto.getGame();
    var previousTime = game.getTimestamp();
    game.updateTimestamp();
    var currentTime = game.getTimestamp();

    var actions = gameDto.getActions();
    if (actions == null) {
      actions = new ArrayList<>();
    }
    // First pass the time
    actions.add(0, new PassTheTime(currentTime - previousTime));
    for (var action : actions) {
      if (!action.isValid(game)) {
        System.err.println("Invalid action: " + action);
        return null;
      }
      action.execute(game);
    }

    // Sign the game
    var resultGameDto = new GameDTO();
    resultGameDto.setGame(game);
    resultGameDto.setSignature(cryptoService.sign(game.toJson()));

    return resultGameDto;
  }
}
