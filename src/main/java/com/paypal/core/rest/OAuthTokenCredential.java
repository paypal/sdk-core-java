package com.paypal.core.rest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.paypal.core.ConfigManager;
import com.paypal.core.ConnectionManager;
import com.paypal.core.Constants;
import com.paypal.core.HttpConfiguration;
import com.paypal.core.HttpConnection;
import com.paypal.core.SDKUtil;
import com.paypal.core.codec.binary.Base64;
import com.paypal.core.credential.ICredential;
import com.paypal.sdk.util.UserAgentHeader;

/**
 * OAuthTokenCredential is used for generation of OAuth Token used by PayPal
 * REST API service. ClientID and ClientSecret are required by the class to
 * generate OAuth Token, the resulting token is of the form "Bearer xxxxxx". The
 * class has two constructors, one of it taking an additional configuration map
 * used for dynamic configuration. When using the constructor with out
 * configuration map the endpoint is fetched from the configuration that is used
 * during initialization. See {@link PayPalResource} for configuring the system.
 * When using a configuration map the class expects an entry by the name
 * "oauth.EndPoint" or "service.EndPoint" to retrieve the value of the endpoint
 * for the OAuth Service. If either are not present the configuration should
 * have a entry by the name "mode" with values sandbox or live wherein the
 * corresponding endpoints are default to PayPal endpoints.
 * 
 * @author kjayakumar
 * 
 */
public final class OAuthTokenCredential implements ICredential {

	/**
	 * OAuth URI path parameter
	 */
	private static String OAUTH_TOKEN_PATH = "/v1/oauth2/token";

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
	 * Sets the URI path for the OAuth Token service. If not set it defaults to
	 * "/v1/oauth2/token"
	 * 
	 * @param oauthTokenPath
	 *            the URI part to set
	 */
	public static void setOAUTH_TOKEN_PATH(String oauthTokenPath) {
		OAUTH_TOKEN_PATH = oauthTokenPath;
	}

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
	 * Configuration Constructor for dynamic configuration
	 * 
	 * @param clientID
	 *            Client ID for the OAuth
	 * @param clientSecret
	 *            Client Secret for OAuth
	 * @param configurationMap
	 *            Dynamic configuration map which should have an entry for
	 *            'oauth.EndPoint' or 'service.EndPoint'. If either are not
	 *            present then there should be entry for 'mode' with values
	 *            sandbox/live, wherein PayPals endpoints are used.
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
	 * and ClientSecret. The token is appended to the token type (Bearer).
	 * 
	 * @return the accessToken
	 * @throws PayPalRESTException
	 */
	public String getAccessToken() throws PayPalRESTException {
		if (accessToken == null) {
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
			headers.put(Constants.AUTHORIZATION_HEADER, "Basic "
					+ base64ClientID);
			headers.put(Constants.HTTP_ACCEPT_HEADER, "*/*");
			UserAgentHeader userAgentHeader = new UserAgentHeader(
					PayPalResource.SDK_ID, PayPalResource.SDK_VERSION);
			headers.putAll(userAgentHeader.getHeader());
			String postRequest = getRequestPayload();
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

	/**
	 * Returns the request payload for OAuth Service. Override this method to
	 * alter the payload
	 * 
	 * @return Payload as String
	 */
	protected String getRequestPayload() {
		return "grant_type=client_credentials";
	}

	/*
	 * Get HttpConfiguration object for OAuth server
	 */
	private HttpConfiguration getOAuthHttpConfiguration() {
		HttpConfiguration httpConfiguration = new HttpConfiguration();
		httpConfiguration
				.setHttpMethod(Constants.HTTP_CONFIG_DEFAULT_HTTP_METHOD);
		String endPointUrl = (configurationMap.get(Constants.OAUTH_ENDPOINT) != null && configurationMap
				.get(Constants.OAUTH_ENDPOINT).trim().length() >= 0) ? configurationMap
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
