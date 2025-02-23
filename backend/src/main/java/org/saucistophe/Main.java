package org.saucistophe;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.saucistophe.increledger.logic.RsaCryptoService;

@QuarkusMain
public class Main {

  public static void main(String[] args) {
    if (args.length > 0 && args[0].equals("generate")) {
      var crypto = new RsaCryptoService(null, null);
      crypto.generateRSAKeyPair();
      System.exit(0);
    } else Quarkus.run(args);
  }
}
