package com.paypal.sdk.openidconnect;

import java.util.HashMap;
import java.util.Map;

import com.paypal.core.ClientCredentials;

public class CreateFromAuthorizationCodeParameters extends ClientCredentials {

	/**
	 * Code
	 */
	private static final String CODE = "code";

	/**
	 * Redirect URI
	 */
	private static final String REDIRECTURI = "redirect_uri";

	/**
	 * Grant Type
	 */
	private static final String GRANTTYPE = "grant_type";

	// Map backing QueryParameters intended to processed
	// by SDK library 'RESTUtil'
	private Map<String, String> containerMap;

	public CreateFromAuthorizationCodeParameters() {
		containerMap = new HashMap<String, String>();
		containerMap.put(GRANTTYPE, "authorization_code");
	}

	/**
	 * @return the containerMap
	 */
	public Map<String, String> getContainerMap() {
		return containerMap;
	}

	/**
	 * Set the code
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		containerMap.put(CODE, code);
	}

	/**
	 * Set the redirect URI
	 * 
	 * @param redirectURI
	 */
	public void setRedirectURI(String redirectURI) {
		containerMap.put(REDIRECTURI, redirectURI);
	}

	/**
	 * Set the Grant Type
	 * 
	 * @param grantType
	 */
	public void setGrantType(String grantType) {
		containerMap.put(GRANTTYPE, grantType);
	}

}
