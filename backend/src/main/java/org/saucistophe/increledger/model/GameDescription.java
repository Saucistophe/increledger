package org.saucistophe.increledger.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class GameDescription {
  public record ResourceDto(
      String name, double amount, double cap, double production, String emoji) {}

  public record OccupationDto(String name, long count, long cap) {}

  public record PopulationDto(
      String name, long count, long cap, long freePopulation, List<OccupationDto> occupations) {}

  public record TechDto(String name, long count, long cap, Map<String, Double> cost) {}

  public record DialogDto(String name, List<String> choices) {}

  List<ResourceDto> resources;
  List<PopulationDto> populations;
  List<TechDto> techs;
  List<DialogDto> dialogs;
}
