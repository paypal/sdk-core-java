package com.paypal.core.rest;

/**
 * PayPalException handles all exceptions related to REST services
 */
public class PayPalRESTException extends HttpErrorException {

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	public PayPalRESTException(String message) {
		super(message);
	}

	public PayPalRESTException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public PayPalRESTException(Throwable throwable) {
		super(throwable);
	}

}
