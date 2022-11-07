package com.ankoki.skjade.commands;

import com.ankoki.skjade.SkJade;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SkJadeCmd implements CommandExecutor {

    private static final String PREFIX = "§fSk§aJade §f| ";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length != 1 && !sender.hasPermission("skjade.admin")) {
            sender.sendMessage(PREFIX + "§7§oYou are currently running §f§oSk§a§oJade §7§ov" +
                    (SkJade.getInstance().isLatest() ? "§a§o" : "§c§o") + SkJade.getInstance().getVersion());
            if (!SkJade.getInstance().isLatest())
                sender.sendMessage(PREFIX + "§e§oYou aren't running the latest version of SkJade on this server. " +
                        "You can find the latest update at https://www.github.com/Ankoki/SkJade/releases/latest");
            if (SkJade.getInstance().isBeta())
                sender.sendMessage(PREFIX + "§e§oDo note that you are currently running an unstable version " +
                        "of this plugin, which is strongly discouraged as it may result in unexpected and/or breaking " +
                        "behavior. Please switch to a stable version if possible!");
        } else {
            switch (args[0].toUpperCase()) {
                case "RELOAD" -> {
                    SkJade.getInstance().getOwnConfig().reloadConfig();
                    sender.sendMessage(PREFIX + "§7§oYou have successfully reloaded the config!");
                }
                case "COPY-TESTS" -> {
                    if (SkJade.getInstance().copyTests())
                        sender.sendMessage(PREFIX + "§7§oYou have copied the SkJade tests to your scripts folder, " +
                                "named 'skjade-tests.sk'. Please reload your scripts to access the command.");
                    else
                        sender.sendMessage(PREFIX + "§c§oThere was an error copying the SkJade tests over. " +
                                "Please check the console for the provided error and report this on the SkJade GitHub if needed.");
                }
            }
        }
        return true;
    }
}