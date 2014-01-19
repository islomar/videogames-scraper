package com.otogami.core;

import java.util.Collection;

import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;

/**
 * Get all the games in Mediamarkt for a specific platform.
 * 
 * @param platform
 * @return Collection<{@link Videogame}>
 */
public interface Robot {

	Collection<Videogame> getVideogamesOnPlatform(Platform platform);
	
}
