package com.ankoki.skjade.commands;

import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SkJadeCmd implements CommandExecutor {
    private static final String PREFIX = Utils.coloured("&8[&eSk&aJade&8] &7");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage(Utils.coloured(PREFIX + "You are currently running §eSk§aJade§7 v"
                + SkJade.getVersion()));
        if (SkJade.isBeta()) {
            sender.sendMessage(Utils.coloured(PREFIX + "Running on a beta version is risky, and " +
                    "can cause data loss or unexpected errors, please switch to a stable version!"));
        }
        //sender.sendMessage(Utils.rainbow("small test string lol", 1));
        return true;
    }
}