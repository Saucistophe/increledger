package org.saucistophe.increledger.logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class CryptoService {

  KeyPair keyPair;

  private static void saveKeyToPEM(String fileName, String header, String footer, byte[] key) {
    String base64Key = Base64.getEncoder().encodeToString(key);
    String pem = header + "\n" + base64Key.replaceAll("(.{64})", "$1\n") + "\n" + footer;
    try {
      Files.write(Paths.get(fileName), pem.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void generateRSAKeyPair() {
    KeyPairGenerator kpGen = null;
    try {
      kpGen = KeyPairGenerator.getInstance("RSA");
      kpGen.initialize(new RSAKeyGenParameterSpec(4096, RSAKeyGenParameterSpec.F4));
    } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
      throw new RuntimeException(e);
    }

    keyPair = kpGen.generateKeyPair();
  }

  public void storeKeys() {
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
  }

  private static PrivateKey loadPrivateKey(String fileName) {

    try {
      String pem = new String(Files.readAllBytes(Paths.get(fileName)));

      String base64Key =
          pem.replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll("\\s", ""); // Remove headers, footers, and newlines
      byte[] keyBytes = Base64.getDecoder().decode(base64Key);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePrivate(keySpec);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }

  private static PublicKey loadPublicKey(String fileName) {

    try {
      String pem = new String(Files.readAllBytes(Paths.get(fileName)));

      String base64Key =
          pem.replace("-----BEGIN PUBLIC KEY-----", "")
              .replace("-----END PUBLIC KEY-----", "")
              .replaceAll("\\s", ""); // Remove headers, footers, and newlines
      byte[] keyBytes = Base64.getDecoder().decode(base64Key);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(keySpec);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }

  public void loadKeys() {
    keyPair = new KeyPair(loadPublicKey("public_key.pem"), loadPrivateKey("private_key.pem"));
  }

  public boolean verify(String originalData, String signature) {
    if (keyPair == null) {
      loadKeys();
    }
    try {
      Signature sig = Signature.getInstance("SHA512withRSA");
      sig.initVerify(keyPair.getPublic());
      sig.update(originalData.getBytes(StandardCharsets.UTF_8));

      return sig.verify(Base64.getDecoder().decode(signature));
    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
      throw new RuntimeException(e);
    }
  }

  public String sign(String dataToSign) {
    if (keyPair == null) {
      loadKeys();
    }
    byte[] data = dataToSign.getBytes(StandardCharsets.UTF_8);

    Signature sig = null;
    try {
      sig = Signature.getInstance("SHA512withRSA");
      sig.initSign(keyPair.getPrivate());
      sig.update(data);
    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
      throw new RuntimeException(e);
    }

    try {
      return Base64.getEncoder().encodeToString(sig.sign());
    } catch (SignatureException e) {
      throw new RuntimeException(e);
    }
  }

  private static KeyPair getKeyPair() throws NoSuchAlgorithmException {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    return kpg.genKeyPair();
  }
}
