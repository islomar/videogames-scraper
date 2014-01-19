package com.otogami.mediamarkt.scraper.robots;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otogami.core.Robot;
import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;
import com.otogami.mediamarkt.scraper.MediamarktConnector;

/**
 * Class responsible for scraping the Mediamarkt website in a single-threaded
 * way.
 * 
 * @author islomar
 * 
 */
public class MediamarktSingleThreadedRobot implements Robot {

	private final static Logger LOG = LoggerFactory.getLogger(MediamarktSingleThreadedRobot.class);

	private MediamarktConnector mediamarktConnector;
	private long startTime;

	public Collection<Videogame> getVideogamesOnPlatform(Platform platform) {

		startTime = System.currentTimeMillis();

		Collection<Videogame> videogameList = new ArrayList<Videogame>();

		mediamarktConnector = new MediamarktConnector(platform);

		int pageNumber = 1;
		while (isVideogamesLeft()) {
			videogameList.addAll(mediamarktConnector.getVideogamesForPage(pageNumber++));
		}

		LOG.info("Total number of videogames scraped for platform " + platform + ": " + videogameList.size());

		LOG.info("Total time elapsed in seconds: " + getElapsedTime());

		return videogameList;
	}

	private boolean isVideogamesLeft() {

		return mediamarktConnector.isVideogamesLeft();
	}

	public double getElapsedTime() {
		long endTime = System.currentTimeMillis();
		return (double) (endTime - startTime) / (1000);
	}
}
