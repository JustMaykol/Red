package dev.maykol.red.user.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static dev.maykol.red.user.UserManager.INSTANCE;

/**
 * @author Maykol Morales Morante (zSirSpectro)
 * Sunday, January 30, 2022
 */

public class UserListener implements Listener {

	@EventHandler( priority = EventPriority.LOWEST )
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		final UUID uniqueId = event.getUniqueId();

		INSTANCE.defaultLoad(uniqueId);

		if (INSTANCE.getUser(uniqueId) == null) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Cannot load you User data.");
		}
	}

	@EventHandler( priority = EventPriority.MONITOR )
	public void onPlayerQuit(PlayerQuitEvent event) {
		INSTANCE.defaultSave(event.getPlayer().getUniqueId());
	}
}
