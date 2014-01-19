package com.otogami.server.dao;

import java.util.List;

import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;
import com.otogami.server.model.VideogameEntity;

public interface VideogameDao {

	// Sample methods
	VideogameEntity findById(Long id);

	VideogameEntity findByStoreGameId(String storeId, String id);

	List<VideogameEntity> findByPlatform(Platform platform);

	void update(List<VideogameEntity> videogameList) throws Exception;

	List<VideogameEntity> findByCriteria(Videogame criteria);
}
