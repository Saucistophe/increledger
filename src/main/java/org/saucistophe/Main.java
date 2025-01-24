package org.saucistophe;

import org.saucistophe.increledger.logic.CryptoService;

public class Main {

  public static void main(String[] args) {
    if (args.length > 0 && args[0].equals("generate")) {
      var crypto = new CryptoService();
      crypto.generateRSAKeyPair();
      crypto.storeKeys();
      System.exit(0);
    }
  }
}
