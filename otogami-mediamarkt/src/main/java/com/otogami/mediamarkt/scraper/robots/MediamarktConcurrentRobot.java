package com.otogami.mediamarkt.scraper.robots;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otogami.core.Robot;
import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;
import com.otogami.mediamarkt.scraper.MediamarktConnector;

/**
 * Class responsible for scraping the Mediamarkt website in a multi-threaded
 * way.
 * 
 * @author islomar
 * 
 */
public class MediamarktConcurrentRobot implements Robot {

	private final static Logger LOG = LoggerFactory.getLogger(MediamarktConcurrentRobot.class);

	private long startTime;

	public Collection<Videogame> getVideogamesOnPlatform(Platform platform) {
		startTime = System.currentTimeMillis();
		Set<Videogame> videogameList = new HashSet<Videogame>();

		int totalNumberOfPages = getTotalNumberOfPages(platform);
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		final List<Callable<Object>> tasks = new ArrayList<Callable<Object>>(totalNumberOfPages);

		for (int pageNumber = 1; pageNumber <= totalNumberOfPages; pageNumber++) {
			Runnable worker = new MediamarktWorkerThread(pageNumber, videogameList, new MediamarktConnector(platform));
			tasks.add(Executors.callable(worker));
		}

		waitWhileAllThreadsAreFinished(executor, tasks);
		executor.shutdown();

		LOG.info("Finished all threads");
		LOG.info("Total number of videogames parsed for platform " + platform + ": " + videogameList.size());
		LOG.info("Total time elapsed in seconds: " + getElapsedTime());
		return videogameList;
	}

	private int getTotalNumberOfPages(Platform platform) {

		MediamarktConnector mediamarktConnector = new MediamarktConnector(platform);
		int totalNumberOfPages = mediamarktConnector.getTotalNumberOfPages();
		LOG.info("Total number of threads to be run in parallel (one for each page to scrape): " + totalNumberOfPages);
		return totalNumberOfPages;
	}

	private void waitWhileAllThreadsAreFinished(ExecutorService executor, List<Callable<Object>> tasks) {
		try {
			executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			LOG.error("Uppsssss... something went wrong!", e);
		}
	}

	public double getElapsedTime() {
		long endTime = System.currentTimeMillis();
		return (double) (endTime - startTime) / (1000);
	}

}
