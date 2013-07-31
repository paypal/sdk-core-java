package com.paypal.core.rest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.paypal.core.ConfigManager;
import com.paypal.core.ConnectionManager;
import com.paypal.core.Constants;
import com.paypal.core.HttpConfiguration;
import com.paypal.core.HttpConnection;
import com.paypal.core.SDKUtil;
import com.paypal.core.credential.ICredential;

public final class OAuthTokenCredential implements ICredential {

	/**
	 * OAuth URI path parameter
	 */
	private static final String OAUTH_TOKEN_PATH = "/v1/oauth2/token";

	/**
	 * Client ID for OAuth
	 */
	private String clientID;

	/**
	 * Client Secret for OAuth
	 */
	private String clientSecret;

	/**
	 * Access Token that is generated
	 */
	private String accessToken;

	/**
	 * Map used for dynamic configuration
	 */
	private Map<String, String> configurationMap;

	/**
	 * @param clientID
	 *            Client ID for the OAuth
	 * @param clientSecret
	 *            Client Secret for OAuth
	 */
	public OAuthTokenCredential(String clientID, String clientSecret) {
		super();
		this.clientID = clientID;
		this.clientSecret = clientSecret;
		this.configurationMap = SDKUtil.combineDefaultMap(ConfigManager
				.getInstance().getConfigurationMap());
	}

	/**
	 * @param clientID
	 *            Client ID for the OAuth
	 * @param clientSecret
	 *            Client Secret for OAuth
	 */
	public OAuthTokenCredential(String clientID, String clientSecret,
			Map<String, String> configurationMap) {
		super();
		this.clientID = clientID;
		this.clientSecret = clientSecret;
		this.configurationMap = SDKUtil.combineDefaultMap(configurationMap);
	}

	/**
	 * Computes Access Token by placing a call to OAuth server using ClientID
	 * and ClientSecret. The token is appended to the token type.
	 *
	 * @return the accessToken
	 * @throws PayPalRESTException
	 */
	public String getAccessToken() throws PayPalRESTException {
		if (accessToken == null) {
			// Write Logic for passing in Detail to Identity Api Serv and
			// computing the token
			// Set the Value inside the accessToken and result
			accessToken = generateAccessToken();
		}
		return accessToken;
	}

	private String generateAccessToken() throws PayPalRESTException {
		String generatedToken = null;
		String base64ClientID = generateBase64String(clientID + ":"
				+ clientSecret);
		generatedToken = generateOAuthToken(base64ClientID);
		return generatedToken;
	}

	/*
	 * Generate a Base64 encoded String from clientID & clientSecret
	 */
	private String generateBase64String(String clientID)
			throws PayPalRESTException {
		String base64ClientID = null;
		byte[] encoded = null;
		try {
			encoded = Base64.encodeBase64(clientID.getBytes("UTF-8"));
			base64ClientID = new String(encoded, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new PayPalRESTException(e.getMessage(), e);
		}
		return base64ClientID;
	}

	/*
	 * Generate OAuth type token from Base64Client ID
	 */
	private String generateOAuthToken(String base64ClientID)
			throws PayPalRESTException {
		HttpConnection connection = null;
		HttpConfiguration httpConfiguration = null;
		String generatedToken = null;
		try {
			connection = ConnectionManager.getInstance().getConnection();
			httpConfiguration = getOAuthHttpConfiguration();
			connection.createAndconfigureHttpConnection(httpConfiguration);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", "Basic " + base64ClientID);
			headers.put(Constants.HTTP_ACCEPT_HEADER, "*/*");
			headers.put("User-Agent", RESTConfiguration.formUserAgentHeader());
			String postRequest = "grant_type=client_credentials";
			String jsonResponse = connection.execute("", postRequest, headers);
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(jsonResponse);
			generatedToken = jsonElement.getAsJsonObject().get("token_type")
					.getAsString()
					+ " "
					+ jsonElement.getAsJsonObject().get("access_token")
							.getAsString();
		} catch (Exception e) {
			throw new PayPalRESTException(e.getMessage(), e);
		}
		return generatedToken;
	}

	/*
	 * Get HttpConfiguration object for OAuth server
	 */
	private HttpConfiguration getOAuthHttpConfiguration() {
		HttpConfiguration httpConfiguration = new HttpConfiguration();
		httpConfiguration
				.setHttpMethod(Constants.HTTP_CONFIG_DEFAULT_HTTP_METHOD);
		String endPointUrl = (configurationMap.get(Constants.OAUTH_ENDPOINT) != null
				&& configurationMap.get(Constants.OAUTH_ENDPOINT).trim()
						.length() >= 0) ? configurationMap
				.get(Constants.OAUTH_ENDPOINT) : configurationMap
				.get(Constants.ENDPOINT);
		if (endPointUrl == null || endPointUrl.trim().length() <= 0) {
			String mode = configurationMap.get(Constants.MODE);
			if (Constants.SANDBOX.equalsIgnoreCase(mode)) {
				endPointUrl = Constants.REST_SANDBOX_ENDPOINT;
			} else if (Constants.LIVE.equalsIgnoreCase(mode)) {
				endPointUrl = Constants.REST_LIVE_ENDPOINT;
			}
		}
		if (Boolean
			.parseBoolean(configurationMap.get(Constants.USE_HTTP_PROXY))) {
		    httpConfiguration.setProxySet(true);
		    httpConfiguration.setProxyHost(configurationMap
			    .get(Constants.HTTP_PROXY_HOST));
		    httpConfiguration.setProxyPort(Integer.parseInt(configurationMap
			    .get(Constants.HTTP_PROXY_PORT)));

		    String proxyUserName = configurationMap
			    .get(Constants.HTTP_PROXY_USERNAME);
		    String proxyPassword = configurationMap
			    .get(Constants.HTTP_PROXY_PASSWORD);

		    if (proxyUserName != null && proxyPassword != null) {
			httpConfiguration.setProxyUserName(proxyUserName);
			httpConfiguration.setProxyPassword(proxyPassword);
		    }
		}
		endPointUrl = (endPointUrl.endsWith("/")) ? endPointUrl.substring(0,
				endPointUrl.length() - 1) : endPointUrl;
		endPointUrl += OAUTH_TOKEN_PATH;
		httpConfiguration.setEndPointUrl(endPointUrl);
		httpConfiguration
				.setGoogleAppEngine(Boolean.parseBoolean(configurationMap
						.get(Constants.GOOGLE_APP_ENGINE)));
		return httpConfiguration;
	}

}
