package dev.maykol.red.user.adapter.implement;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.maykol.red.Red;
import dev.maykol.red.user.User;
import dev.maykol.red.user.adapter.UserAdapter;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

import static dev.maykol.red.user.UserManager.INSTANCE;

/**
 * @author Maykol Morales Morante (zSirSpectro)
 * Sunday, January 30, 2022
 */

public class DocumentAdapter implements UserAdapter<Document> {

	private static final ReplaceOptions OPTION = new ReplaceOptions().upsert(true);

	private final MongoCollection<Document> collection;

	public DocumentAdapter() {
		final FileConfiguration config = Red.INSTANCE.getConfig();
		final MongoDatabase mongoDatabase;

		if (config.getBoolean("MONGO.AUTH.ENABLED")) {
			ServerAddress serverAddress = new ServerAddress(config.getString("MONGO.HOST"), config.getInt("MONGO.PORT"));
			MongoCredential credential = MongoCredential.createCredential(config.getString("MONGO.AUTH.USER"), "admin", config.getString("MONGO.AUTH.PASS").toCharArray());

			mongoDatabase = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build()).getDatabase("Red");
		} else {
			mongoDatabase = new MongoClient(config.getString("MONGO.HOST"), config.getInt("MONGO.PORT")).getDatabase("Red");
		}

		collection = mongoDatabase.getCollection("user");
	}

	@Override
	public void defaultLoad(UUID uniqueId) {
		if (INSTANCE.getStorage().containsKey(uniqueId)) return;

		Document document = collection.find(Filters.eq("uniqueId", uniqueId)).first();

		if (document == null) {
			INSTANCE.getStorage().put(uniqueId, new User(uniqueId));
			return;
		}

		INSTANCE.getStorage().put(uniqueId, fromData(document));
	}

	@Override
	public void defaultSave(UUID uniqueId, User user) {
		if (user == null) return;

		collection.replaceOne(Filters.eq("uniqueId", uniqueId), toData(user), OPTION);

		INSTANCE.getStorage().remove(uniqueId);
	}

	@Override
	public Document toData(User user) {
		final Document document = new Document();

		document.append("uniqueId", user.getUniqueId().toString());

		document.append("name", user.getName());

		document.append("prefix", user.getPrefix());
		document.append("suffix", user.getSuffix());

		document.append("killCount", user.getKillCount());
		document.append("deathCount", user.getDeathCount());

		document.append("money", user.getMoney());

		return document;
	}

	@Override
	public User fromData(Document document) {
		final UUID uniqueId = UUID.fromString(document.getString("uniqueId"));
		final User user = new User(uniqueId);

		user.setName(document.getString("name"));

		user.setPrefix(document.getString("prefix"));
		user.setSuffix(document.getString("suffix"));

		user.setKillCount(document.getInteger("killCount"));
		user.setDeathCount(document.getInteger("deathCount"));

		user.setMoney(document.getDouble("money"));

		return user;
	}

	@Override
	public void saveStored() {
	}

	@Override
	public void deleteStored() {
	}
}
