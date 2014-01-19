package com.otogami.mediamarkt.scraper.robots;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;
import com.otogami.mediamarkt.scraper.robots.MediamarktSingleThreadedRobot;

public class MediamarktSingleThreadedRobotTest {

	private MediamarktSingleThreadedRobot robot = new MediamarktSingleThreadedRobot();

	private static Videogame lastOfUs = new Videogame();
	private static Videogame borderlands2 = new Videogame();
	static {
		lastOfUs.setId("10014089");
		borderlands2.setId("10003868");
	}

	@Test
	public void testContainsLastOfUs() {
		Collection<Videogame> videogamesPs3 = robot.getVideogamesOnPlatform(Platform.ps3);
		assertTrue(videogamesPs3.contains(lastOfUs));
	}

	@Ignore
	@Test
	public void testContainsBorderlands2() {
		Collection<Videogame> videogamesXbox360 = robot.getVideogamesOnPlatform(Platform.xbox360);
		assertTrue(videogamesXbox360.contains(borderlands2));
	}

}
