package org.saucistophe.increledger.logic;

import io.quarkus.test.junit.QuarkusTestProfile;

public class RsaCryptoProfile implements QuarkusTestProfile {
  @Override
  public String getConfigProfile() {
    return "secure";
  }
}
