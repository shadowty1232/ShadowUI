package uk.co.tmdavies.shadowui.events;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.co.tmdavies.shadowui.ShadowUI;
import uk.co.tmdavies.shadowui.utils.Utils;

public class PlayerListener implements Listener {


	private final ShadowUI plugin;
	private boolean papiEnabled;

	public PlayerListener(ShadowUI plugin) {

		this.plugin = plugin;
		this.papiEnabled = plugin.papiEnabled;

		Bukkit.getPluginManager().registerEvents(this, plugin);

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		FastBoard board = new FastBoard(p);

		if (papiEnabled) {

			board.updateTitle(IridiumColorAPI.process(PlaceholderAPI.setPlaceholders(p, ShadowUI.config.getConfig().getString("Board.Title"))));

		} else {

			board.updateTitle(IridiumColorAPI.process(ShadowUI.config.getConfig().getString("Board.Title")));

		}

		Utils.setPlayerListHeader(p);

		if (p.hasPermission("shadowui.customtabname")) {

			p.setPlayerListName(p.getDisplayName());

		}

		plugin.updateBoards(p, board);

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {

		Player p = e.getPlayer();

		FastBoard board = plugin.boards.remove(p.getUniqueId());

		if (board != null) {

			board.delete();

		}

	}

}
