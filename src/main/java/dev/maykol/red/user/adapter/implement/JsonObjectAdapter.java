package dev.maykol.red.user.adapter.implement;

import com.google.gson.JsonObject;
import dev.maykol.red.Red;
import dev.maykol.red.user.User;
import dev.maykol.red.user.adapter.UserAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static dev.maykol.red.Red.GSON;
import static dev.maykol.red.user.UserManager.INSTANCE;

/**
 * @author Maykol Morales Morante (zSirSpectro)
 * Sunday, January 30, 2022
 */

public class JsonObjectAdapter implements UserAdapter<JsonObject> {

	private static final File PARENT = new File(Red.INSTANCE.getDataFolder(), "user");

	@Override
	public void defaultLoad(UUID uniqueId) {
		if (INSTANCE.getStorage().containsKey(uniqueId)) return;

		File file = new File(PARENT, uniqueId + ".json");

		if (!file.exists()) {
			INSTANCE.getStorage().put(uniqueId, new User(uniqueId));
			return;
		}

		try (FileReader reader = new FileReader(file)) {
			INSTANCE.getStorage().put(uniqueId, fromData(GSON.fromJson(reader, JsonObject.class)));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void defaultSave(UUID uniqueId, User user) {
		if (user == null) return;

		File file = getOrCreateFile(uniqueId);

		try (FileWriter writer = new FileWriter(file)) {
			GSON.toJson(toData(user), writer);
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		INSTANCE.getStorage().remove(uniqueId);
	}

	@Override
	public JsonObject toData(User user) {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("uniqueId", user.getUniqueId().toString());

		jsonObject.addProperty("name", user.getName());

		jsonObject.addProperty("prefix", user.getPrefix());
		jsonObject.addProperty("suffix", user.getSuffix());

		jsonObject.addProperty("killCount", user.getKillCount());
		jsonObject.addProperty("deathCount", user.getDeathCount());

		jsonObject.addProperty("money", user.getMoney());

		return jsonObject;
	}

	@Override
	public User fromData(JsonObject object) {
		final UUID uniqueId = UUID.fromString(object.get("uniqueId").getAsString());
		final User user = new User(uniqueId);

		user.setName(object.get("name").getAsString());

		user.setPrefix(object.get("prefix").getAsString());
		user.setSuffix(object.get("suffix").getAsString());

		user.setKillCount(object.get("killCount").getAsInt());
		user.setDeathCount(object.get("deathCount").getAsInt());

		user.setMoney(object.get("money").getAsDouble());

		return user;
	}

	@Override
	public void saveStored() {
		for (Map.Entry<UUID, User> entry : INSTANCE.getStorage().entrySet()) {
			final UUID uniqueId = entry.getKey();
			final User user = entry.getValue();

			defaultSave(uniqueId, user);
		}
	}

	@Override
	public void deleteStored() {
		PARENT.delete();
		INSTANCE.getStorage().clear();

		Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).forEach(uniqueId -> INSTANCE.getStorage().put(uniqueId, new User(uniqueId)));
	}

	private File getOrCreateFile(UUID uniqueId) {
		if (!PARENT.exists()) PARENT.mkdirs();

		File file = new File(PARENT, uniqueId + ".json");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}

		return file;
	}
}
