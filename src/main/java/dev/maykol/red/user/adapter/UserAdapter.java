package dev.maykol.red.user.adapter;

import dev.maykol.red.user.User;

import java.util.UUID;

import static dev.maykol.red.user.UserManager.INSTANCE;

/**
 * @author Maykol Morales Morante (zSirSpectro)
 * Sunday, January 30, 2022
 */

public interface UserAdapter<R> {

	void defaultLoad(UUID uniqueId);

	default void defaultSave(UUID uniqueId) {
		defaultSave(uniqueId, INSTANCE.getStorage().get(uniqueId));
	}

	void defaultSave(UUID uniqueId, User user);

	R toData(User user);

	User fromData(R r);

	void saveStored();

	void deleteStored();

}
