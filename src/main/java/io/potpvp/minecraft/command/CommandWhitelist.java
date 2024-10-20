package io.potpvp.minecraft.command;

import static io.potpvp.minecraft.core.CoreConstants.Database.ENTITY_REMOVAL_ERROR;
import static io.potpvp.minecraft.core.CoreConstants.General.NO_PERMISSION;
import static io.potpvp.minecraft.core.CoreConstants.General.PREFIX;
import static io.potpvp.minecraft.core.CoreConstants.Service.WHITELIST_REMOVE;

import io.potpvp.minecraft.command.bridge.CommandArguments;
import io.potpvp.minecraft.command.bridge.SubCommand;
import io.potpvp.minecraft.command.bridge.annotations.MainCommand;
import io.potpvp.minecraft.core.CorePlayer;
import io.potpvp.minecraft.database.ResultHandler;
import io.potpvp.minecraft.entity.whitelist.WhitelistEntity;
import io.potpvp.minecraft.service.whitelist.WhitelistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This command is used to manage the whitelist.
 *
 * @author Rysefoxx
 * @since 14.10.2024
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@MainCommand(name = "whitelist")
public class CommandWhitelist extends SubCommand {

  private final WhitelistService whitelistService;

  @Override
  public void execute(@NotNull CommandSender sender, @Nullable CorePlayer corePlayer, @NotNull CommandArguments arguments) {
    if (corePlayer == null) {
      return;
    }

    if (!corePlayer.hasPermission("whitelist")) {
      corePlayer.translate(NO_PERMISSION);
      return;
    }

    if (arguments.isLength(2) && arguments.equalsIgnoreCase(0, "add")) {
      this.whitelistService.addUser(corePlayer, arguments.element(1));
      return;
    }

    if (arguments.isLength(2) && arguments.equalsIgnoreCase(0, "remove")) {
      this.whitelistService.removeUser(corePlayer, arguments.element(1), new ResultHandler<>() {
        @Override
        public void success(WhitelistEntity entity) {
          corePlayer.translate(WHITELIST_REMOVE, PREFIX, arguments.element(1));
        }

        @Override
        public void error(WhitelistEntity entity, Throwable throwable) {
          ResultHandler.super.error(entity, throwable);
          corePlayer.translate(ENTITY_REMOVAL_ERROR);
        }
      });
      return;
    }

    if (arguments.isLength(1) && arguments.equalsIgnoreCase(0, "toggle")) {
      this.whitelistService.toggle(corePlayer);
      return;
    }

    if (arguments.isLength(1) && arguments.equalsIgnoreCase(0, "list")) {
      this.whitelistService.list(corePlayer);
      return;
    }

    corePlayer.sendHelp("whitelist toggle", "whitelist add <User>", "whitelist remove <User>", "whitelist list");
  }

  @Override
  protected @Nullable List<String> tabComplete(@NotNull CorePlayer corePlayer, @NotNull CommandArguments arguments) {
    if (!corePlayer.hasPermission("whitelist")) {
      return List.of();
    }

    if (arguments.isLength(1)) {
      return List.of("toggle", "add", "remove", "list");
    }

    if (arguments.isLength(2)) {
      if (arguments.equalsIgnoreCase(0, "add")) {
        return null;
      }

      if (arguments.equalsIgnoreCase(0, "remove")) {
        return this.whitelistService.getCache().stream().map(entity -> entity.getUuid().getNameOrDefault()).toList();
      }
    }

    return List.of();
  }
}