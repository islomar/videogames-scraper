package com.otogami.frontend.exception;

public class VideogameSearchException extends Exception {

	private static final long serialVersionUID = -3284819972554358268L;

	public VideogameSearchException(String message, Throwable throwable) {

		super(message, throwable);
	}

	public VideogameSearchException(String message) {

		super(message);
	}

}
