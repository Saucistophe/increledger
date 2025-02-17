package org.saucistophe.increledger.logic;

import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

@TestProfile(RsaCryptoProfile.class)
@QuarkusTest
class RsaCryptoServiceTest {

  @Inject CryptoService cryptoService;

  @Test
  void generateRSAKeyPair() {
    assertInstanceOf(RsaCryptoService.class, cryptoService);
    ((RsaCryptoService) cryptoService).generateRSAKeyPair();
    var signature = cryptoService.sign("test");
    assertTrue(signature.length() > 100);
    assertTrue(cryptoService.verify("test", signature));
  }

  @Test
  void signAndVerify() {
    var initialData = RandomStringUtils.insecure().nextAscii(10000);
    var signature = cryptoService.sign(initialData);
    assertTrue(cryptoService.verify(initialData, signature));
    assertFalse(cryptoService.verify(initialData + "a", signature));
  }

  @Test
  void invalidSignature() {

    assertThrows(
        BadRequestException.class,
        () -> cryptoService.verify("Whatever the data", "An in valid signature"));
  }
}
