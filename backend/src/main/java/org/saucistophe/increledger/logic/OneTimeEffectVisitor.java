package org.saucistophe.increledger.logic;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.saucistophe.increledger.model.Game;
import org.saucistophe.increledger.model.rules.GameRules;
import org.saucistophe.increledger.model.rules.effects.AddDialog;
import org.saucistophe.increledger.model.rules.effects.AddMessage;
import org.saucistophe.increledger.model.rules.effects.AddTech;
import org.saucistophe.increledger.model.rules.effects.IncreasePopulationOnce;

@ApplicationScoped
@RequiredArgsConstructor
public class OneTimeEffectVisitor {

  private final GameRules gameRules;
  private final GameComputingService gameService;

  public boolean isValid(AddDialog effect) {
    return gameRules.getDialogById(effect.getDialog()) != null;
  }

  public void execute(AddDialog effect, Game game) {
    game.getDialogs().add(effect.getDialog());
  }

  public boolean isValid(AddMessage effect) {
    return true;
  }

  public void execute(AddMessage effect, Game game) {
    game.getMessages().add(effect.getMessage());
  }

  public boolean isValid(IncreasePopulationOnce effect) {
    return gameRules.getPopulationById(effect.getTarget()) != null;
  }

  public void execute(IncreasePopulationOnce effect, Game game) {
    var caps = gameService.getPopulationCaps(game);
    var target = effect.getTarget();
    var amount = effect.getAmount();

    game.getPopulations().putIfAbsent(target, 0L);
    var newAmount = Math.min(game.getPopulations().get(target) + amount, caps.get(target));
    game.getPopulations().put(target, newAmount);
  }

  public boolean isValid(AddTech effect) {
    return gameRules.getTechById(effect.getTech()) != null;
  }

  public void execute(AddTech effect, Game game) {
    var caps = gameService.getTechCaps(game);
    var tech = effect.getTech();
    var count = effect.getCount();

    game.getTechs().putIfAbsent(tech, 0L);
    var newCount = Math.min(game.getTechs().get(tech) + count, caps.get(tech));
    game.getTechs().put(tech, newCount);
  }
}
