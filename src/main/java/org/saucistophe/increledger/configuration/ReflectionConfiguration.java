package org.saucistophe.increledger.configuration;

import com.fasterxml.jackson.databind.EnumNamingStrategies;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {EnumNamingStrategies.CamelCaseStrategy.class})
public class ReflectionConfiguration {}
