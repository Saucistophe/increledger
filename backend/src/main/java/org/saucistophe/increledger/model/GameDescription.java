package org.saucistophe.increledger.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class GameDescription {
  public record ResourceDto(
      String name,
      String translation,
      double amount,
      double cap,
      double production,
      String emoji) {}

  public record OccupationDto(String name, String translation, long count, long cap) {}

  public record PopulationDto(
      String name,
      String translation,
      long count,
      long cap,
      long freePopulation,
      List<OccupationDto> occupations) {}

  public record TechDto(
      String name, String translation, long count, long cap, Map<String, Double> cost) {}

  public record DialogDto(String name, String translation, List<DialogChoiceDto> choices) {}

  public record DialogChoiceDto(String name, String translation) {}

  private String title;
  private String timeSpent;
  private List<ResourceDto> resources;
  private List<PopulationDto> populations;
  private List<TechDto> techs;
  private List<DialogDto> dialogs;
}
