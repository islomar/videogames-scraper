package com.otogami.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;
import com.otogami.server.dao.VideogameDao;
import com.otogami.server.dao.VideogameDatabaseDao;
import com.otogami.server.model.VideogameEntity;

public class VideogameService {

	private VideogameDao dao;

	public VideogameService() {
		dao = new VideogameDatabaseDao();
	}

	public VideogameService(VideogameDao dao) {
		this.dao = dao;
	}

	public List<VideogameEntity> findByPlatform(Platform platform) {

		return dao.findByPlatform(platform);
	}

	public List<VideogameEntity> findByCriteria(Videogame criteria) {

		return dao.findByCriteria(criteria);
	}

	public void update(Collection<Videogame> videogameList) throws Exception {

		List<VideogameEntity> entityList = convertToEntityList(videogameList);

		dao.update(entityList);
	}

	private List<VideogameEntity> convertToEntityList(Collection<Videogame> videogameList) {

		List<VideogameEntity> entityList = new ArrayList<VideogameEntity>(videogameList.size());
		for (Videogame videogame : videogameList) {
			VideogameEntity entity = new VideogameEntity(videogame);
			entityList.add(entity);
		}
		return entityList;
	}

}
