package org.saucistophe.increledger.model;

import java.util.List;
import lombok.Data;

@Data
public class GameDescription {
  public record ResourceDto(String name, double amount, double cap, double production) {}
  public record PopulationDto(String name, long count, long cap) {}
  public record TechDto(String name, long count, long cap) {} // TODO only onlocked ones
  public record OccupationDto(String name, String population, long count, long cap) {}

  List<ResourceDto> resources;
  List<PopulationDto> populations;
  List<TechDto> techs;
}
