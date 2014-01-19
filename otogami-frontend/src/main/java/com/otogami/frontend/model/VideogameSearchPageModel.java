package com.otogami.frontend.model;

import java.util.List;

import com.otogami.core.model.Availability;
import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;

/**
 * Model to be mapped into the View.
 * 
 * @author islomar
 * 
 */
public class VideogameSearchPageModel {

	private List<Videogame> videogames;
	private String errorMessage;
	private InputFormModel input;
	private Platform[] platformList;
	private Availability[] availabilityList;

	public List<Videogame> getVideogames() {
		return videogames;
	}

	public void setVideogames(List<Videogame> videogames) {
		this.videogames = videogames;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public InputFormModel getInput() {
		return input;
	}

	public void setInput(InputFormModel input) {
		this.input = input;
	}

	public Platform[] getPlatformList() {
		return Platform.values();
	}

	public Availability[] getAvailabilityList() {
		return Availability.values();
	}

}
