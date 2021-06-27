package com.ankoki.skjade.commands;

import com.ankoki.skjade.SkJade;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SkJadeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && (sender.isOp() || sender.hasPermission("skjade.admin"))) {
            SkJade.getInstance().getOwnConfig().reloadConfig();
            sender.sendMessage("§fSk§aJade §f| §7§oYou have sucessfully reloaded the config!");
        } else {
            sender.sendMessage("§fSk§aJade §f| §7§oYou are currently running §f§oSk§a§oJade §7§ov" +
                    (SkJade.getInstance().isLatest() ? "§a§o" : "§c§o") + SkJade.getInstance().getVersion());
            if (SkJade.getInstance().isBeta()) {
                sender.sendMessage("§fSk§aJade §f| §7§oDo note that you are currently running an unstable version " +
                        "of this plugin, which is strongly discouraged as it may result in unexpected and/or breaking " +
                        "behavior. Please switch to a stable version if possible!");
            }
        }
        return true;
    }
}