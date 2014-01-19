package com.otogami.mediamarkt.scraper.robots;

import java.util.Collection;

import com.otogami.core.model.Videogame;
import com.otogami.mediamarkt.scraper.MediamarktConnector;

/**
 * Class representing one thread searching for a specific pageNumber in
 * Mediamarkt videogame website.
 * 
 * @author islomar
 * 
 */
public class MediamarktWorkerThread implements Runnable {

	private int pageNumber;
	private Collection<Videogame> videogameList;
	private MediamarktConnector mediamarktConnector;

	public MediamarktWorkerThread(int pageNumber, Collection<Videogame> videogameList, MediamarktConnector mediamarktConnector) {
		this.pageNumber = pageNumber;
		this.videogameList = videogameList;
		this.mediamarktConnector = mediamarktConnector;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " Start. Page number = " + pageNumber);
		processCommand();
		System.out.println(Thread.currentThread().getName() + " End. Page number = " + pageNumber);
	}

	private void processCommand() {
		System.out.println("Parsing the page " + pageNumber + "...");
		videogameList.addAll(mediamarktConnector.getVideogamesForPage(pageNumber++));
	}

	@Override
	public String toString() {
		return "Pagenumber: " + pageNumber;
	}
}