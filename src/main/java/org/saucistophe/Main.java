package org.saucistophe;

import org.saucistophe.increledger.logic.CryptoService;

public class Main {

  public static void main(String[] args) {
    if (args.length > 0 && args[0].equals("generate")) {
      // TODO
      var crypto = new CryptoService(null, null);
      crypto.generateRSAKeyPair();
      System.exit(0);
    }
  }
}
