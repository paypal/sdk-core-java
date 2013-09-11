package com.paypal.sdk.openidconnect;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.paypal.core.Constants;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.HttpMethod;
import com.paypal.core.rest.JSONFormatter;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;
import com.paypal.core.rest.RESTUtil;

public class Tokeninfo {

	/**
	 * OPTIONAL, if identical to the scope requested by the client; otherwise,
	 * REQUIRED.
	 */
	private String scope;

	/**
	 * The access token issued by the authorization server.
	 */
	private String accessToken;

	/**
	 * The refresh token, which can be used to obtain new access tokens using
	 * the same authorization grant as described in OAuth2.0 RFC6749 in Section
	 * 6.
	 */
	private String refreshToken;

	/**
	 * The type of the token issued as described in OAuth2.0 RFC6749 (Section
	 * 7.1). Value is case insensitive.
	 */
	private String tokenType;

	/**
	 * The lifetime in seconds of the access token.
	 */
	private Integer expiresIn;

	/**
	 * Returns the last request sent to the Service
	 * 
	 * @return Last request sent to the server
	 */
	public static String getLastRequest() {
		return PayPalResource.getLastRequest();
	}

	/**
	 * Returns the last response returned by the Service
	 * 
	 * @return Last response got from the Service
	 */
	public static String getLastResponse() {
		return PayPalResource.getLastResponse();
	}

	/**
	 * Initialize using InputStream(of a Properties file)
	 * 
	 * @param is
	 *            InputStream
	 * @throws PayPalRESTException
	 */
	public static void initConfig(InputStream is) throws PayPalRESTException {
		PayPalResource.initConfig(is);
	}

	/**
	 * Initialize using a File(Properties file)
	 * 
	 * @param file
	 *            File object of a properties entity
	 * @throws PayPalRESTException
	 */
	public static void initConfig(File file) throws PayPalRESTException {
		PayPalResource.initConfig(file);
	}

	/**
	 * Initialize using Properties
	 * 
	 * @param properties
	 *            Properties object
	 */
	public static void initConfig(Properties properties) {
		PayPalResource.initConfig(properties);
	}

	/**
	 * Default Constructor
	 */
	public Tokeninfo() {
	}

	/**
	 * Parameterized Constructor
	 */
	public Tokeninfo(String accessToken, String tokenType, Integer expiresIn) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
	}

	/**
	 * Setter for scope
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * Getter for scope
	 */
	public String getScope() {
		return this.scope;
	}

	/**
	 * Setter for accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Getter for accessToken
	 */
	public String getAccessToken() {
		return this.accessToken;
	}

	/**
	 * Setter for refreshToken
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * Getter for refreshToken
	 */
	public String getRefreshToken() {
		return this.refreshToken;
	}

	/**
	 * Setter for tokenType
	 */
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	/**
	 * Getter for tokenType
	 */
	public String getTokenType() {
		return this.tokenType;
	}

	/**
	 * Setter for expiresIn
	 */
	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	/**
	 * Getter for expiresIn
	 */
	public Integer getExpiresIn() {
		return this.expiresIn;
	}

	/**
	 * Creates an Access Token from an Authorization Code.
	 * 
	 * @param createFromAuthorizationCodeParameters
	 *            Query parameters used for API call
	 * @return Tokeninfo
	 * @throws PayPalRESTException
	 */
	public static Tokeninfo createFromAuthorizationCode(
			CreateFromAuthorizationCodeParameters createFromAuthorizationCodeParameters)
			throws PayPalRESTException {
		String pattern = "v1/identity/openidconnect/tokenservice?grant_type={0}&code={1}&redirect_uri={2}";
		Object[] parameters = new Object[] { createFromAuthorizationCodeParameters };
		String resourcePath = RESTUtil.formatURIPath(pattern, parameters);
		String payLoad = resourcePath.substring(resourcePath.indexOf('?') + 1);
		resourcePath = resourcePath.substring(0, resourcePath.indexOf('?'));
		Map<String, String> headersMap = new HashMap<String, String>();
		if (createFromAuthorizationCodeParameters.getClientID() == null
				|| createFromAuthorizationCodeParameters.getClientID().trim()
						.length() <= 0
				|| createFromAuthorizationCodeParameters.getClientSecret() == null
				|| createFromAuthorizationCodeParameters.getClientSecret()
						.trim().length() <= 0) {
			throw new PayPalRESTException(
					"ClientID and ClientSecret not set in CreateFromAuthorizationCodeParameters");
		} else {
			OAuthTokenCredential oauthTokenCredential = new OAuthTokenCredential(
					createFromAuthorizationCodeParameters.getClientID(),
					createFromAuthorizationCodeParameters.getClientSecret());
			String authorizationHeader = oauthTokenCredential
					.getAuthorizationHeader();
			headersMap.put("Authorization", authorizationHeader);
		}
		headersMap.put(Constants.HTTP_CONTENT_TYPE_HEADER,
				Constants.HTTP_CONFIG_DEFAULT_CONTENT_TYPE);
		return PayPalResource.configureAndExecute(null, HttpMethod.POST,
				resourcePath, headersMap, payLoad, Tokeninfo.class);
	}

	/**
	 * Creates an Access Token from an Authorization Code.
	 * 
	 * @param apiContext
	 *            {@link APIContext} to be used for the call.
	 * @param createFromAuthorizationCodeParameters
	 *            Query parameters used for API call
	 * @return Tokeninfo
	 * @throws PayPalRESTException
	 */
	public static Tokeninfo createFromAuthorizationCode(
			APIContext apiContext,
			CreateFromAuthorizationCodeParameters createFromAuthorizationCodeParameters)
			throws PayPalRESTException {
		String pattern = "v1/identity/openidconnect/tokenservice?grant_type={0}&code={1}&redirect_uri={2}";
		Object[] parameters = new Object[] { createFromAuthorizationCodeParameters };
		String resourcePath = RESTUtil.formatURIPath(pattern, parameters);
		String payLoad = resourcePath.substring(resourcePath.indexOf('?') + 1);
		resourcePath = resourcePath.substring(0, resourcePath.indexOf('?'));
		Map<String, String> headersMap = new HashMap<String, String>();
		if (createFromAuthorizationCodeParameters.getClientID() == null
				|| createFromAuthorizationCodeParameters.getClientID().trim()
						.length() <= 0
				|| createFromAuthorizationCodeParameters.getClientSecret() == null
				|| createFromAuthorizationCodeParameters.getClientSecret()
						.trim().length() <= 0) {
			throw new PayPalRESTException(
					"ClientID and ClientSecret not set in CreateFromAuthorizationCodeParameters");
		} else {
			OAuthTokenCredential oauthTokenCredential = new OAuthTokenCredential(
					createFromAuthorizationCodeParameters.getClientID(),
					createFromAuthorizationCodeParameters.getClientSecret(),
					apiContext.getConfigurationMap());
			String authorizationHeader = oauthTokenCredential
					.getAuthorizationHeader();
			headersMap.put("Authorization", authorizationHeader);
		}
		headersMap.put(Constants.HTTP_CONTENT_TYPE_HEADER,
				Constants.HTTP_CONFIG_DEFAULT_CONTENT_TYPE);
		return PayPalResource.configureAndExecute(apiContext, HttpMethod.POST,
				resourcePath, headersMap, payLoad, Tokeninfo.class);
	}

	/**
	 * Creates an Access Token from an Refresh Token.
	 * 
	 * @param createFromRefreshTokenParameters
	 *            Query parameters used for API call
	 * @return Tokeninfo
	 * @throws PayPalRESTException
	 */
	public Tokeninfo createFromRefreshToken(
			CreateFromRefreshTokenParameters createFromRefreshTokenParameters)
			throws PayPalRESTException {
		String pattern = "v1/identity/openidconnect/tokenservice?grant_type={0}&refresh_token={1}&scope={2}&client_id={3}&client_secret={4}";
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.putAll(createFromRefreshTokenParameters.getContainerMap());
		try {
			paramsMap.put("refresh_token",
					URLEncoder.encode(getRefreshToken(), "UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			// Ignore
		}
		Object[] parameters = new Object[] { paramsMap };
		String resourcePath = RESTUtil.formatURIPath(pattern, parameters);
		String payLoad = resourcePath.substring(resourcePath.indexOf('?') + 1);
		resourcePath = resourcePath.substring(0, resourcePath.indexOf('?'));
		Map<String, String> headersMap = new HashMap<String, String>();
		if (createFromRefreshTokenParameters.getClientID() == null
				|| createFromRefreshTokenParameters.getClientID().trim()
						.length() <= 0
				|| createFromRefreshTokenParameters.getClientSecret() == null
				|| createFromRefreshTokenParameters.getClientSecret().trim()
						.length() <= 0) {
			throw new PayPalRESTException(
					"ClientID and ClientSecret not set in CreateFromRefreshTokenParameters");
		} else {
			OAuthTokenCredential oauthTokenCredential = new OAuthTokenCredential(
					createFromRefreshTokenParameters.getClientID(),
					createFromRefreshTokenParameters.getClientSecret());
			String authorizationHeader = oauthTokenCredential
					.getAuthorizationHeader();
			headersMap.put("Authorization", authorizationHeader);
		}
		headersMap.put(Constants.HTTP_CONTENT_TYPE_HEADER,
				Constants.HTTP_CONFIG_DEFAULT_CONTENT_TYPE);
		return PayPalResource.configureAndExecute(null, HttpMethod.POST,
				resourcePath, headersMap, payLoad, Tokeninfo.class);
	}

	/**
	 * Creates an Access Token from an Refresh Token.
	 * 
	 * @param apiContext
	 *            {@link APIContext} to be used for the call.
	 * @param createFromRefreshTokenParameters
	 *            Query parameters used for API call
	 * @return Tokeninfo
	 * @throws PayPalRESTException
	 */
	public Tokeninfo createFromRefreshToken(APIContext apiContext,
			CreateFromRefreshTokenParameters createFromRefreshTokenParameters)
			throws PayPalRESTException {
		String pattern = "v1/identity/openidconnect/tokenservice?grant_type={0}&refresh_token={1}&scope={2}&client_id={3}&client_secret={4}";
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.putAll(createFromRefreshTokenParameters.getContainerMap());
		try {
			paramsMap.put("refresh_token",
					URLEncoder.encode(getRefreshToken(), "UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			// Ignore
		}
		Object[] parameters = new Object[] { paramsMap };
		String resourcePath = RESTUtil.formatURIPath(pattern, parameters);
		String payLoad = resourcePath.substring(resourcePath.indexOf('?') + 1);
		resourcePath = resourcePath.substring(0, resourcePath.indexOf('?'));
		Map<String, String> headersMap = new HashMap<String, String>();
		if (createFromRefreshTokenParameters.getClientID() == null
				|| createFromRefreshTokenParameters.getClientID().trim()
						.length() <= 0
				|| createFromRefreshTokenParameters.getClientSecret() == null
				|| createFromRefreshTokenParameters.getClientSecret().trim()
						.length() <= 0) {
			throw new PayPalRESTException(
					"ClientID and ClientSecret not set in CreateFromRefreshTokenParameters");
		} else {
			OAuthTokenCredential oauthTokenCredential = new OAuthTokenCredential(
					createFromRefreshTokenParameters.getClientID(),
					createFromRefreshTokenParameters.getClientSecret(),
					apiContext.getConfigurationMap());
			String authorizationHeader = oauthTokenCredential
					.getAuthorizationHeader();
			headersMap.put("Authorization", authorizationHeader);
		}
		headersMap.put(Constants.HTTP_CONTENT_TYPE_HEADER,
				Constants.HTTP_CONFIG_DEFAULT_CONTENT_TYPE);
		return PayPalResource.configureAndExecute(apiContext, HttpMethod.POST,
				resourcePath, headersMap, payLoad, Tokeninfo.class);
	}

	/**
	 * Returns a JSON string corresponding to object state
	 * 
	 * @return JSON representation
	 */
	public String toJSON() {
		return JSONFormatter.toJSON(this);
	}

	@Override
	public String toString() {
		return toJSON();
	}
}