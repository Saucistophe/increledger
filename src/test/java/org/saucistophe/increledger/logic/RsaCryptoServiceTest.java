package org.saucistophe.increledger.logic;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@TestProfile(RsaCryptoProfile.class)
@QuarkusTest
class RsaCryptoServiceTest {
  @Inject
  CryptoService cryptoService;

  @Test
  void generateRSAKeyPair() {
    assertInstanceOf(RsaCryptoService.class, cryptoService);
    ((RsaCryptoService) cryptoService).generateRSAKeyPair();
    var signature =cryptoService.sign("test");
    assertTrue(signature.length()> 100);
    assertTrue(cryptoService.verify("test", signature));
    }

}
