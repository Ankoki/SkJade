package com.ankoki.skjade.commands;

import com.ankoki.skjade.SkJade;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SkJadeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage("§8[§6Sk§aJade§8] §7You are currently running §eSk§aJade§7 v"
                + SkJade.getVersion());
        if (SkJade.isBeta()) {
            sender.sendMessage("§8[§6Sk§aJade§8] §7Running on a beta version is risky, and " +
                    "can cause data loss or unexpected errors, please switch to a stable version if possible!");
        }
        return true;
    }
}