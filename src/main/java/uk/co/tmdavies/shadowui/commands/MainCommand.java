package uk.co.tmdavies.shadowui.commands;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.tmdavies.shadowui.ShadowUI;
import uk.co.tmdavies.shadowui.utils.Utils;

public class MainCommand implements CommandExecutor {

	private final ShadowUI plugin;
	private boolean papiEnabled;

	public MainCommand(ShadowUI plugin) {

		this.plugin = plugin;
		this.papiEnabled = ShadowUI.papiEnabled;
		plugin.getCommand("shadowui").setExecutor(this);

	}

	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

		if (!sender.hasPermission("shadowui.admin")) {

			sender.sendMessage(IridiumColorAPI.process("<GRADIENT:FF0000>You do not have permission to execute this command</GRADIENT:830000>"));
			return true;

		}

		if (args.length != 1) {

			invalidArgs(sender);
			return true;

		}

		if (args[0].equalsIgnoreCase("reload")) {

			ShadowUI.config.reloadConfig();

			sender.sendMessage(IridiumColorAPI.process("<GRADIENT:00FF00>Successfully reloaded the Config file.</GRADIENT:008300>"));

			if (sender instanceof Player) {

				plugin.boards.clear();

				for (Player ppl : Bukkit.getOnlinePlayers()) {

					FastBoard board = new FastBoard(ppl);

					if (papiEnabled) {

						board.updateTitle(IridiumColorAPI.process(PlaceholderAPI.setPlaceholders(ppl, ShadowUI.config.getConfig().getString("Board.Title"))));

					} else {

						board.updateTitle(IridiumColorAPI.process(ShadowUI.config.getConfig().getString("Board.Title")));

					}

					plugin.boards.put(ppl.getUniqueId(), board);

					Utils.setPlayerListHeader(ppl);

				}

			}

		}

		return true;

	}

	public void invalidArgs(CommandSender sender) {

		Utils.sendPlayerCenteredMessage(sender, "");
		Utils.sendPlayerCenteredMessage(sender, "&7&lShadow&4&lUI");
		Utils.sendPlayerCenteredMessage(sender, "&8&oby Carbonate");
		Utils.sendPlayerCenteredMessage(sender, "");
		Utils.sendPlayerCenteredMessage(sender, "&cUsages:");
		Utils.sendPlayerCenteredMessage(sender, "&c/shadowui reload");
		Utils.sendPlayerCenteredMessage(sender, "");

	}

}
