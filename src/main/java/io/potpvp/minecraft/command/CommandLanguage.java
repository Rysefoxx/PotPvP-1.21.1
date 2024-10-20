package io.potpvp.minecraft.command;


import static io.potpvp.minecraft.core.CoreConstants.Command.LANGUAGE_INVALID;
import static io.potpvp.minecraft.core.CoreConstants.Command.LANGUAGE_SELECTED;
import static io.potpvp.minecraft.core.CoreConstants.General.PREFIX;

import io.potpvp.minecraft.command.bridge.CommandArguments;
import io.potpvp.minecraft.command.bridge.SubCommand;
import io.potpvp.minecraft.command.bridge.annotations.MainCommand;
import io.potpvp.minecraft.core.CorePlayer;
import io.potpvp.minecraft.entity.language.LanguageEntity;
import io.potpvp.minecraft.language.Language;
import io.potpvp.minecraft.service.language.LanguageService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This command is used to change the language of the player.
 *
 * @author Rysefoxx
 * @since 19.10.2024
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@MainCommand(name = "language", alias = {"sprache", "lang"})
public class CommandLanguage extends SubCommand {

  private final LanguageService languageService;

  @Override
  public void execute(@NotNull CommandSender sender, @Nullable CorePlayer corePlayer, @NotNull CommandArguments arguments) {
    if (corePlayer == null) {
      return;
    }

    if (!arguments.isLength(1)) {
      corePlayer.sendHelp("language <Language>");
      return;
    }

    String input = arguments.element(0);
    Optional<Language> optional = Language.getLanguage(input);
    if (optional.isEmpty()) {
      corePlayer.translate(LANGUAGE_INVALID, PREFIX, input);
      return;
    }

    LanguageEntity entity = languageService.getOrCreate(corePlayer.getUuid());
    Language language = optional.get();
    entity.setLanguage(language);
    corePlayer.translate(LANGUAGE_SELECTED, PREFIX, language.toString());
  }

  @Override
  protected @Nullable List<String> tabComplete(@NotNull CorePlayer corePlayer, @NotNull CommandArguments arguments) {
    if (!arguments.isLength(1)) {
      return List.of();
    }

    return Language.getLanguages();
  }
}