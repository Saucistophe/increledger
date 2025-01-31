package org.saucistophe.increledger.logic;

import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
@UnlessBuildProfile("secure")
public class NoOpCryptoService implements CryptoService {
  @Override
  public boolean verify(String originalData, String signature) {
    return true;
  }

  @Override
  public String sign(String dataToSign) {
    return "Dummy Signature";
  }
}
