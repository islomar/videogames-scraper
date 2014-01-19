package com.otogami.mediamarkt.scraper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;
import com.otogami.mediamarkt.scraper.MediamarktConnector;
import com.otogami.mediamarkt.scraper.MediamarktVideogamesParser;

public class MediamarktConnectorTest {

	private static final int TOTAL_NUMBER_OF_VIDEOGAMES = 291;

	@Mock
	private WebClient webClient;
	
	@Mock
	private HtmlPage htmlPage;

	@Mock
	private MediamarktVideogamesParser parser;
	
	@InjectMocks
	private MediamarktConnector mediamarktConnector;

	private int totalNumberOfPages;

	private Set<Videogame> videogames;
	
	@Before
	public final void setUp() {
		mediamarktConnector = new MediamarktConnector(Platform.ps3);
		initMocks(this);
	}

	@Test
	public void assertPageWithoutVideogamesReturnsZeroVideogames() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		givenTheInitialStateOfVideogamesLeftIsCorrect();
		givenAnExistingHtmlPage();
		givenTheHtmlPageHasNoVideogames();
		
		whenGetVideogamesForPageIsCalled();
		
		thenNoVideogamesAreScraped();
		thenNoVideogamesAreLeft();
	}

	private void givenTheInitialStateOfVideogamesLeftIsCorrect() {
		assertTrue(mediamarktConnector.isVideogamesLeft());
	}

	private void thenNoVideogamesAreLeft() {
		assertFalse(mediamarktConnector.isVideogamesLeft());
	}

	private void thenNoVideogamesAreScraped() {
		assertEquals(0, videogames.size());
	}

	private void whenGetVideogamesForPageIsCalled() {
		videogames = mediamarktConnector.getVideogamesForPage(999);
	}

	private void givenTheHtmlPageHasNoVideogames() {
		when(parser.getUnparsedVideogameList(any(HtmlPage.class))).thenReturn(new ArrayList<HtmlAnchor>());
	}
	
	@Test
	public void assertTheTotalNumberOfPagesIsCorrectlyCalculated() throws FailingHttpStatusCodeException, IOException {
		givenAnExistingHtmlPage();
		givenThePageHasVideogames();
		
		whenGetTotalNumberOfPages();
		
		thenTheTotalNumberOfPagesMatchesTheExpectedResult();
	}

	private void thenTheTotalNumberOfPagesMatchesTheExpectedResult() {
		assertEquals(Math.ceil((double)TOTAL_NUMBER_OF_VIDEOGAMES/MediamarktConnector.NUMBER_OF_RESULTS_PER_PAGE), totalNumberOfPages, 0);
	}

	private void whenGetTotalNumberOfPages() {
		totalNumberOfPages = mediamarktConnector.getTotalNumberOfPages();
	}

	private void givenThePageHasVideogames() {
		when(parser.getTotalNumberOfVideogames(any(HtmlPage.class))).thenReturn(TOTAL_NUMBER_OF_VIDEOGAMES);
	}

	private void givenAnExistingHtmlPage() throws IOException {
		when(webClient.getPage(any(WebRequest.class))).thenReturn(htmlPage);
	}
}
