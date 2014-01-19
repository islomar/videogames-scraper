package com.otogami.frontend.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otogami.core.model.Availability;
import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;
import com.otogami.frontend.exception.VideogameSearchException;
import com.otogami.frontend.facade.VideogameSearchFacade;
import com.otogami.frontend.model.InputFormModel;
import com.otogami.frontend.model.VideogameSearchPageModel;

public class VideogameSearchServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(VideogameSearchServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		LOG.info("doGet()");
		fillModelAndDispatch(request, response, new ArrayList<Videogame>(), null);
	}

	private void fillModelAndDispatch(HttpServletRequest request, HttpServletResponse response, List<Videogame> videogameList, String errorMessage)
			throws ServletException, IOException {
		VideogameSearchPageModel model = createModel(request, videogameList, errorMessage);
		request.setAttribute("model", model);

		request.getRequestDispatcher("/WEB-INF/templates/index.ftl").forward(request, response);
	}

	private VideogameSearchPageModel createModel(HttpServletRequest request, List<Videogame> videogameList, String errorMessage) {
		VideogameSearchPageModel model = new VideogameSearchPageModel();
		model.setVideogames(videogameList);
		model.setErrorMessages(errorMessage);
		model.setInput(fillInputFormModel(request));
		return model;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Videogame> videogameList = new ArrayList<Videogame>();
		String errorMessage = null;
		try {
			Videogame searchCritera = createSearchCriteria(request);
			List<Videogame> videogamesFound = new VideogameSearchFacade().findVideogames(searchCritera);

			videogameList.clear();
			videogameList.addAll(videogamesFound);
		} catch (ParseException e) {
			errorMessage = "Please, introduce a valid price";
			videogameList.clear();
		} catch (VideogameSearchException e) {
			errorMessage = "Ooooppsss... we had problems when trying to retrieve the games: please try again!!";
			videogameList.clear();
		} catch (Exception e) {
			errorMessage = "Ooooppsss... we're sorry, something unexpected happened: please try again!!";
			videogameList.clear();
		}

		fillModelAndDispatch(request, response, videogameList, errorMessage);
	}

	private InputFormModel fillInputFormModel(HttpServletRequest request) {
		InputFormModel inputFormModel = new InputFormModel();
		inputFormModel.setPrice(request.getParameter("price"));
		inputFormModel.setTitle(request.getParameter("title"));
		inputFormModel.setPlatform(request.getParameter("platform"));
		inputFormModel.setAvailability(request.getParameter("availability"));
		return inputFormModel;
	}

	private Videogame createSearchCriteria(HttpServletRequest request) throws ParseException {

		Videogame videogame = new Videogame();
		videogame.setTitle(request.getParameter("title"));
		videogame.setPlatform(getPlatform(request));
		videogame.setAvailability(getAvailability(request));
		videogame.setPrice(getPrice(request));
		LOG.info("Searching for a videogame meeting this criteria:" + videogame);
		return videogame;
	}

	private BigDecimal getPrice(HttpServletRequest request) throws ParseException {
		NumberFormat nf = NumberFormat.getInstance(new Locale("es", "ES"));
		String price = request.getParameter("price");
		if (StringUtils.isNotBlank(price)) {
			return new BigDecimal(nf.parse(price).toString());
		}
		return null;
	}

	private Platform getPlatform(HttpServletRequest request) {
		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform) && !platform.equalsIgnoreCase(Platform.all.toString())) {
			return Platform.valueOf(platform);
		}
		return null;
	}

	private Availability getAvailability(HttpServletRequest request) {
		String availability = request.getParameter("availability");
		if (StringUtils.isNotBlank(availability) && !availability.equalsIgnoreCase(Availability.Any.toString())) {
			return Availability.valueOf(availability);
		}
		return null;
	}
}
