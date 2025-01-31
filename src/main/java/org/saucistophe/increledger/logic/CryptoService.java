package org.saucistophe.increledger.logic;

public interface CryptoService {
  boolean verify(String originalData, String signature);

  String sign(String dataToSign);
}
