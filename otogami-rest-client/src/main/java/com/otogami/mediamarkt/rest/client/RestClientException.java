package com.otogami.mediamarkt.rest.client;


public class RestClientException extends Exception {

	private static final long serialVersionUID = -3284819972554358268L;

    public RestClientException(String message, Throwable throwable) {

        super(message, throwable);
    }

}
