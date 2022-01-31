package dev.maykol.red.user;

import dev.maykol.red.Red;
import dev.maykol.red.user.adapter.UserAdapter;
import dev.maykol.red.user.adapter.implement.DocumentAdapter;
import dev.maykol.red.user.adapter.implement.JsonObjectAdapter;
import dev.maykol.red.user.listener.UserListener;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Maykol Morales Morante (zSirSpectro)
 * Sunday, January 30, 2022
 */

@Getter
public enum UserManager {

	INSTANCE;

	@Setter private UserAdapter<?> adapter;

	private final Map<UUID, User> storage = new HashMap<>();

	public void onEnable(Red red) {
		String type = red.getConfig().getString("STORAGE");

		switch (type) {
			case "MONGO":
				adapter = new DocumentAdapter();
				break;

			case "JSON":
				adapter = new JsonObjectAdapter();
				break;

			default:
				throw new IllegalArgumentException("Unknown STORAGE type.");
		}

		red.getServer().getPluginManager().registerEvents(new UserListener(), red);
	}

	public void onDisable(Red red) {
		saveStored();
	}

	public User getUser(UUID uniqueId) {
		return storage.get(uniqueId);
	}

	public void defaultLoad(UUID uniqueId) {
		adapter.defaultLoad(uniqueId);
	}

	public void defaultSave(UUID uniqueId) {
		adapter.defaultSave(uniqueId);
	}

	public void saveStored() {
		adapter.saveStored();
	}

	public void deleteStored() {
		adapter.deleteStored();
	}
}
