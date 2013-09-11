package com.paypal.core;

/**
 * <code>ClientCredentials</code> holds Client ID and Client Secret
 * 
 * @author kjayakumar
 * 
 */
public class ClientCredentials {

	/**
	 * Client ID
	 */
	private String clientID;

	/**
	 * Client Secret
	 */
	private String clientSecret;

	public ClientCredentials() {
		super();
	}

	/**
	 * @return the clientID
	 */
	public String getClientID() {
		return clientID;
	}

	/**
	 * @param clientID
	 *            the clientID to set
	 */
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	/**
	 * @return the clientSecret
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * @param clientSecret
	 *            the clientSecret to set
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

}
