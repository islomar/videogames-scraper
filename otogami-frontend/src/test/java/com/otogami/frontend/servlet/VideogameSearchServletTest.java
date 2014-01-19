package com.otogami.frontend.servlet;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.otogami.core.model.Platform;
import com.otogami.mediamarkt.rest.client.RestClientException;
import com.otogami.mediamarkt.rest.client.VideogamesRestClient;

public class VideogameSearchServletTest {

	@Mock
	private VideogamesRestClient restClient;

	@Mock
	private Response restResponse;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Before
	public final void setUp() {
		new VideogameSearchServlet();
		initMocks(this);
	}

	@Ignore
	@Test
	public void unfinishedExample() throws ServletException, IOException, RestClientException {

		givenAnHttpServletRequest();
		givenAnHttpServletResponse();
		givenASearchByPlatform(Platform.ps3);
		givenARestServerWorkingFine();
		// TODO - More stuff

		whenServletGetsCalled();

		thenResponseContainsVideogameList();
		// TODO - More stuff
	}

	private void thenResponseContainsVideogameList() {
		// verify(request, atLeast(1)).getParameter("platform");
	}

	private void givenASearchByPlatform(Platform platform) {
		when(request.getParameter("platform")).thenReturn(platform.toString());
	}

	private void whenServletGetsCalled() throws ServletException, IOException {
		new VideogameSearchServlet().doGet(request, response);
	}

	private void givenARestServerWorkingFine() throws RestClientException {
		URI uri = URI.create("http://xxxx");
		when(restClient.doGet(uri)).thenReturn(restResponse);
	}

	private void givenAnHttpServletResponse() {
		new MockHttpServletResponse();
	}

	private void givenAnHttpServletRequest() {
		request = new MockHttpServletRequest();
	}

}
