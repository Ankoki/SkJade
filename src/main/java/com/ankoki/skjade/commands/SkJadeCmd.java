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
				case "HELP" -> {
					sender.sendMessage(PREFIX + "§7§oThere are a few commands you can use, and are listed as followed.\n" +
							" §7- §a/skjade §7~ §f§oThis shows the current version and warnings if they are needed.\n" +
							" §7- §a/skjade reload §7~ §f§oThis reloads the config.yml in the 'plugins/SkJade' folder.\n" +
							" §7- §a/skjade contact §7~ §f§oThis shows all the applicable ways to contact the developer (me:D).\n" +
							" §7- §a/skjade help §7~ §f§oYou are here! This shows the command help list.\n" +
							PREFIX + "§7§oBut this is a skript addon, what more do you need:)");
				}
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
				case "CONTACT" -> {
					sender.sendMessage(PREFIX + "§7§oIf you ever need to contact me in regards to SkJade or any other reason, you can reach out through the following methods:\n" +
							" §7- §aBYEOL DEV §7~ §fThis is my development discord, and you can join it through this link: discord.gg/3RWFg2xDBF\n" +
							" §7- §aSkUnity §7~ §fThis is the official Skript discord, you can join it at discord.gg/skript\n" +
							" §7- §aDiscord DMs §7~ §fMy discord tag is Ankoki#0001 if you ever need support.\n" +
							" §7- §aGitHub §7~ §fIf there is an issue or suggestion, you should report it at the GitHub error tracker, at www.github.com/Ankoki/SkJade/issues");
				}
				default -> sender.sendMessage(PREFIX + "§c§oThere is no command that matches the argument '" + args[0] + "'");
			}
		}
		return true;
	}
}