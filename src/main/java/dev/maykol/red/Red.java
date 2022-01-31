package dev.maykol.red;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.maykol.red.user.UserManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Maykol Morales Morante (zSirSpectro)
 * Sunday, January 30, 2022
 */

public class Red extends JavaPlugin {

	public static Red INSTANCE;

	public static Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

	@Override
	public void onEnable() {
		INSTANCE = this;

		saveDefaultConfig();

		UserManager.INSTANCE.onEnable(this);
	}

	@Override
	public void onDisable() {
		UserManager.INSTANCE.onDisable(this);

		INSTANCE = null;
	}
}
