package dev.maykol.red.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * @author Maykol Morales Morante (zSirSpectro)
 * Sunday, January 30, 2022
 */

@Getter @Setter
@Accessors( chain = true )
@RequiredArgsConstructor
public class User {

	private final UUID uniqueId;

	private String name = "null";

	private String prefix = "null";
	private String suffix = "null";

	private int killCount;
	private int deathCount;

	private double money;

}
