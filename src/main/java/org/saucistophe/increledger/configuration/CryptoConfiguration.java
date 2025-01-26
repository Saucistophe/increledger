package org.saucistophe.increledger.configuration;

import io.quarkus.logging.Log;
import io.quarkus.runtime.configuration.ConfigurationException;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CryptoConfiguration {

  @Singleton
  public PrivateKey loadPrivateKey() {
    try {
      String pem = new String(Files.readAllBytes(Paths.get("private_key.pem")));

      String base64Key =
          pem.replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll("\\s", ""); // Remove headers, footers, and newlines
      byte[] keyBytes = Base64.getDecoder().decode(base64Key);
      var keySpec = new PKCS8EncodedKeySpec(keyBytes);
      var keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePrivate(keySpec);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      Log.error("Could not load key from PEM file", e);
      throw new ConfigurationException(e);
    }
  }

  @Singleton
  public PublicKey loadPublicKey() {
    try {
      String pem = new String(Files.readAllBytes(Paths.get("public_key.pem")));

      String base64Key =
          pem.replace("-----BEGIN PUBLIC KEY-----", "")
              .replace("-----END PUBLIC KEY-----", "")
              .replaceAll("\\s", ""); // Remove headers, footers, and newlines
      byte[] keyBytes = Base64.getDecoder().decode(base64Key);
      var keySpec = new X509EncodedKeySpec(keyBytes);
      var keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(keySpec);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      Log.error("Could not load key from PEM file", e);
      throw new ConfigurationException(e);
    }
  }
}
