package org.saucistophe.increledger.logic;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
@IfBuildProfile("secure")
public class RsaCryptoService implements CryptoService {

  private final PrivateKey privateKey;
  private final PublicKey publicKey;

  private static void saveKeyToPEM(String fileName, String header, String footer, byte[] key) {
    String base64Key = Base64.getEncoder().encodeToString(key);
    String pem = header + "\n" + base64Key.replaceAll("(.{64})", "$1\n") + "\n" + footer;
    try {
      Files.write(Paths.get(fileName), pem.getBytes());
    } catch (IOException e) {
      Log.error("Could not save key to PEM file", e);
      throw new InternalServerErrorException(e);
    }
  }

  public void generateRSAKeyPair() {
    try {
      var kpGen = KeyPairGenerator.getInstance("RSA");
      kpGen.initialize(new RSAKeyGenParameterSpec(4096, RSAKeyGenParameterSpec.F4));
      var keyPair = kpGen.generateKeyPair();

      saveKeyToPEM(
          "private_key.pem",
          "-----BEGIN PRIVATE KEY-----",
          "-----END PRIVATE KEY-----",
          keyPair.getPrivate().getEncoded());
      saveKeyToPEM(
          "public_key.pem",
          "-----BEGIN PUBLIC KEY-----",
          "-----END PUBLIC KEY-----",
          keyPair.getPublic().getEncoded());
    } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
      Log.error("Could not generate key pair", e);
      throw new InternalServerErrorException(e);
    }
  }

  public boolean verify(String originalData, String signature) {
    try {
      Signature sig = Signature.getInstance("SHA512withRSA");
      sig.initVerify(publicKey);
      sig.update(originalData.getBytes(StandardCharsets.UTF_8));

      return sig.verify(Base64.getDecoder().decode(signature));
    } catch (Exception e) {
      Log.debug("Invalid signature provided", e);
      throw new BadRequestException("The signature is invalid or could not be verified", e);
    }
  }

  public String sign(String dataToSign) {
    byte[] data = dataToSign.getBytes(StandardCharsets.UTF_8);

    try {
      var sig = Signature.getInstance("SHA512withRSA");
      sig.initSign(privateKey);
      sig.update(data);
      return Base64.getEncoder().encodeToString(sig.sign());
    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
      Log.error("Could not sign game", e);
      throw new InternalServerErrorException(e);
    }
  }
}
