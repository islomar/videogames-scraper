package com.otogami.frontend.model;

/**
 * Model for the input parameters in the search form.
 * 
 * @author islomar
 * 
 */
public class InputFormModel {

	private String price;
	private String title;
	private String platform;
	private String availability;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

}
