package uk.co.tmdavies.shadowui;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.shadowui.commands.MainCommand;
import uk.co.tmdavies.shadowui.events.PlayerListener;
import uk.co.tmdavies.shadowui.utils.Config;

import java.util.*;
import java.util.logging.Logger;

public final class ShadowUI extends JavaPlugin {

	public Map<UUID, FastBoard> boards;
	public static Config config;
	public Logger logger;
	public static boolean papiEnabled = true;
	public double configVer = 1.0;

	@Override
	public void onEnable() {

		this.logger = getLogger();
		this.boards = new HashMap<>();
		config = new Config("config");

		checkDepends();
		checkBoards();
		setUpConfig();

		new MainCommand(this);
		new PlayerListener(this);

	}

	public void checkDepends() {

		if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {

			logger.info(IridiumColorAPI.process("<GRADIENT:2C08BA>PlaceholderAPI not found. Placeholders from PAPI cannot be used.</GRADIENT:028A97>"));

			papiEnabled = false;

		}

	}

	public void checkBoards() {

		Bukkit.getScheduler().runTaskTimer(this, () -> {

			for (UUID uuid : boards.keySet()) {

				updateBoards(Bukkit.getPlayer(uuid), boards.get(uuid));

			}

		}, 0, 1);

	}

	public void updateBoards(Player p, FastBoard board) {

		List<String> list = config.getConfig().getStringList("Board.Lines");

		if (papiEnabled) list = PlaceholderAPI.setPlaceholders(p, list);

		list = IridiumColorAPI.process(list);

		String[] lines = list.toArray(new String[0]);

		board.updateLines(lines);

	}

	public void setUpConfig() {

		if (config.getConfig().getDouble("Version") != configVer) {

			config.set("Version", configVer);

			List<String> lines = new ArrayList<>();

			lines.add("&4&m             ✸             ✸             ");
			lines.add(" ");
			lines.add("&7&lShadow&4&lMC");
			lines.add(" ");

			config.set("TabList.Header", lines);

			lines = new ArrayList<>();

			lines.add(" ");
			lines.add("&7play.shadowmc.co.uk");
			lines.add(" ");
			lines.add("&4&m             ✸             ✸             ");

			config.set("TabList.Footer", lines);

			lines = new ArrayList<>();

			lines.add("&4&m             ✸             ✸             ");
			lines.add(" ");
			lines.add("<GRADIENT:FF0000>PLAYER</GRADIENT:830000> &8&l»");
			lines.add(" &7Name: &4%player_name%");
			lines.add(" ");
			lines.add("<GRADIENT:FF0000>ECONOMY</GRADIENT:830000> &8&l»");
			lines.add(" &7Balance: &4%vault_eco_balance%");
			lines.add(" ");
			lines.add("&7&oplay.shadowmc.co.uk");
			lines.add("&4&m             ✸             ✸             ");

			config.set("Board.Title", "&7&lShadow&4&lScoreboard");
			config.set("Board.Lines", lines);

			config.saveConfig();

		}

	}
}
