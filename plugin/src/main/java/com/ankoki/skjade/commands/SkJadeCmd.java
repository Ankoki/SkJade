package com.ankoki.skjade.commands;

import com.ankoki.skjade.SkJade;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SkJadeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && (sender.isOp() || sender.hasPermission("skjade.admin"))) {
            SkJade.getOwnConfig().reloadConfig();
            sender.sendMessage("§8[§6Sk§aJade§8] §7You have sucessfully reloaded the config! Reloading the config does " +
                    "absolutely nothing, as you have to restart the server for the changes to take place anyway:)");
        } else {
            sender.sendMessage("§8[§6Sk§aJade§8] §7You are currently running §eSk§aJade§7 v" + SkJade.getVersion());
            if (SkJade.isBeta()) {
                sender.sendMessage("§8[§6Sk§aJade§8] §7Do note that you are currently running an unstable version " +
                        "of this plugin which is strongly discouraged as it may result in unexpected and/or breaking " +
                        "behavior. Please switch to a stable version if possible!");
            }
        }
        return true;
    }
}