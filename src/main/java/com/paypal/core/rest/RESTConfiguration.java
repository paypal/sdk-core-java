package com.paypal.core.rest;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.paypal.core.Constants;
import com.paypal.core.HttpConfiguration;
import com.paypal.core.SDKUtil;

/**
 * RESTConfiguration helps {@link PayPalResource} with state dependent utility
 * methods
 */
public class RESTConfiguration {

	/**
	 * Java Version and bit header computed during construction
	 */
	private static final String JAVAHEADER;

	/**
	 * OS Version and bit header computed during construction
	 */
	private static final String OSHEADER;

	/**
	 * Configuration Map used for dynamic configuration
	 */
	private Map<String, String> configurationMap = null;

	static {

		// Java Version computed statically
		StringBuilder javaVersion = new StringBuilder("lang=Java");
		if (System.getProperty("java.version") != null
				&& System.getProperty("java.version").length() > 0) {
			javaVersion.append(";v=")
					.append(System.getProperty("java.version"));
		}
		if (System.getProperty("java.vm.name") != null
				&& System.getProperty("java.vm.name").length() > 0) {
			javaVersion.append(";bit=");
			if (System.getProperty("java.vm.name").contains("Client")) {
				javaVersion.append("32");
			} else {
				javaVersion.append("64");
			}
		}
		JAVAHEADER = javaVersion.toString();

		// OS Version Header
		StringBuilder osVersion = new StringBuilder();
		if (System.getProperty("os.name") != null
				&& System.getProperty("os.name").length() > 0) {
			osVersion.append("os=");
			osVersion.append(System.getProperty("os.name").replace(' ', '_'));
		} else {
			osVersion.append("os=");
		}
		if (System.getProperty("os.version") != null
				&& System.getProperty("os.version").length() > 0) {
			osVersion.append(" "
					+ System.getProperty("os.version").replace(' ', '_'));
		}
		OSHEADER = osVersion.toString();
	}

	/**
	 * Base URL for the service
	 */
	private URL url;

	/**
	 * Authorization token
	 */
	private String authorizationToken;

	/**
	 * {@link HttpMethod}
	 */
	private HttpMethod httpMethod;

	/**
	 * Resource URI as defined in the WSDL
	 */
	private String resourcePath;

	/**
	 * Request Id
	 */
	private String requestId;

	/**
	 * Custom headers Map
	 */
	private Map<String, String> headersMap;

	/**
	 * Constructor using configurations dynamically
	 * 
	 * @param configurationMap
	 *            Map used for dynamic configuration
	 */
	public RESTConfiguration(Map<String, String> configurationMap) {
		this.configurationMap = SDKUtil.combineDefaultMap(configurationMap);
	}

	/**
	 * Constructor using a Map of headers for forming custom headers
	 * 
	 * @param configurationMap
	 *            Map used for dynamic configuration
	 * @param headersMap
	 *            Headers Map
	 */
	public RESTConfiguration(Map<String, String> configurationMap,
			Map<String, String> headersMap) {
		this(configurationMap);
		this.headersMap = (headersMap == null) ? Collections
				.<String, String> emptyMap() : headersMap;
	}

	/**
	 * @param authorizationToken
	 *            the authorizationToken to set
	 */
	public void setAuthorizationToken(String authorizationToken) {
		this.authorizationToken = authorizationToken;
	}

	/**
	 * @param httpMethod
	 *            the httpMethod to set
	 */
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * @param resourcePath
	 *            the resourcePath to set
	 */
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	/**
	 * @param requestId
	 *            the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the javaheader
	 */
	public static String getJavaheader() {
		return JAVAHEADER;
	}

	/**
	 * @return the osheader
	 */
	public static String getOsheader() {
		return OSHEADER;
	}

	/**
	 * Returns HTTP headers as a {@link Map}
	 * 
	 * @return {@link Map} of Http headers
	 */
	public Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		if (authorizationToken != null
				&& authorizationToken.trim().length() > 0) {
			headers.put("Authorization", authorizationToken);
		} else if (getClientID() != null && getClientID().trim().length() > 0
				&& getClientSecret() != null
				&& getClientSecret().trim().length() > 0) {
			try {
				headers.put(
						"Authorization",
						"Basic "
								+ encodeToBase64(getClientID(),
										getClientSecret()));
			} catch (UnsupportedEncodingException e) {
				//
			}
		}
		headers.put("User-Agent", formUserAgentHeader());
		if (requestId != null && requestId.length() > 0) {
			headers.put("PayPal-Request-Id", requestId);
		}
		if (headersMap != null && headersMap.size() > 0) {
			headers.putAll(headersMap);
		}
		return headers;
	}

	/**
	 * Returns a {@link HttpConfiguration} based on configuration
	 * 
	 * @return {@link HttpConfiguration}
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	public HttpConfiguration getHttpConfigurations()
			throws MalformedURLException, URISyntaxException {
		HttpConfiguration httpConfiguration = new HttpConfiguration();
		httpConfiguration.setHttpMethod(httpMethod.toString());
		httpConfiguration.setEndPointUrl(getBaseURL().toURI()
				.resolve(resourcePath).toString());
		httpConfiguration.setContentType((headersMap != null) ? ((headersMap
				.get(Constants.HTTP_CONTENT_TYPE_HEADER) != null) ? headersMap
				.get("Content-Type") : Constants.HTTP_CONTENT_TYPE_JSON)
				: Constants.HTTP_CONTENT_TYPE_JSON);
		httpConfiguration.setGoogleAppEngine(Boolean
				.parseBoolean(this.configurationMap
						.get(Constants.GOOGLE_APP_ENGINE)));
		if (Boolean.parseBoolean(this.configurationMap
				.get((Constants.USE_HTTP_PROXY)))) {
			httpConfiguration.setProxyPort(Integer
					.parseInt(this.configurationMap
							.get((Constants.HTTP_PROXY_PORT))));
			httpConfiguration.setProxyHost(this.configurationMap
					.get((Constants.HTTP_PROXY_HOST)));
			httpConfiguration.setProxyUserName(this.configurationMap
					.get((Constants.HTTP_PROXY_USERNAME)));
			httpConfiguration.setProxyPassword(this.configurationMap
					.get((Constants.HTTP_PROXY_PASSWORD)));
		}
		httpConfiguration.setConnectionTimeout(Integer
				.parseInt(this.configurationMap
						.get(Constants.HTTP_CONNECTION_TIMEOUT)));
		httpConfiguration.setMaxRetry(Integer.parseInt(this.configurationMap
				.get(Constants.HTTP_CONNECTION_RETRY)));
		httpConfiguration.setReadTimeout(Integer.parseInt(this.configurationMap
				.get(Constants.HTTP_CONNECTION_READ_TIMEOUT)));
		httpConfiguration.setMaxHttpConnection(Integer
				.parseInt(this.configurationMap
						.get(Constants.HTTP_CONNECTION_MAX_CONNECTION)));
		httpConfiguration.setIpAddress(this.configurationMap
				.get(Constants.DEVICE_IP_ADDRESS));
		return httpConfiguration;
	}

	/**
	 * Returns the base URL configured in application resources
	 * 
	 * @return Base {@link URL}
	 * @throws MalformedURLException
	 */
	public URL getBaseURL() throws MalformedURLException {
		if (url == null) {
			String urlString = this.configurationMap.get("service.EndPoint");
			if (urlString == null || urlString.length() <= 0) {
				String mode = this.configurationMap.get("mode");
				if ("sandbox".equalsIgnoreCase(mode)) {
					urlString = Constants.REST_SANDBOX_ENDPOINT;
				} else if ("live".equalsIgnoreCase(mode)) {
					urlString = Constants.REST_LIVE_ENDPOINT;
				} else {
					throw new MalformedURLException(
							"service.EndPoint not set (OR) mode not configured to sandbox/live ");
				}
			}
			if (!urlString.endsWith("/")) {
				urlString += "/";
			}
			url = new URL(urlString);
		}
		return url;
	}

	/**
	 * @param urlString
	 *            the url to set
	 */
	public void setUrl(String urlString) throws MalformedURLException {
		if (urlString != null && urlString.length() > 0) {
			String uString = urlString.endsWith("/") ? urlString : urlString
					+ "/";
			this.url = new URL(uString);
		} else {
			this.url = getBaseURL();
		}
	}

	/**
	 * Returns User-Agent header
	 * 
	 * @return
	 */
	public static String formUserAgentHeader() {
		String header = null;
		StringBuilder stringBuilder = new StringBuilder("PayPalSDK/"
				+ PayPalResource.SDK_ID + " " + PayPalResource.SDK_VERSION
				+ " ");
		stringBuilder.append("(").append(JAVAHEADER);
		String osVersion = OSHEADER;
		if (osVersion.length() > 0) {
			stringBuilder.append(";").append(osVersion);
		}
		stringBuilder.append(")");
		header = stringBuilder.toString();
		return header;
	}

	/*
	 * Return Client ID from configuration Map
	 */
	private String getClientID() {
		return this.configurationMap.get(Constants.CLIENT_ID);
	}

	/*
	 * Returns Client Secret from configuration Map
	 */
	private String getClientSecret() {
		return this.configurationMap.get(Constants.CLIENT_SECRET);
	}

	/*
	 * Encodes Client ID and Client Secret in Base 64
	 */
	private String encodeToBase64(String clientID, String clientSecret)
			throws UnsupportedEncodingException {
		String base64ClientID = generateBase64String(clientID + ":"
				+ clientSecret);
		return base64ClientID;
	}

	/*
	 * Generate a Base64 encoded String from clientID & clientSecret
	 */
	private String generateBase64String(String clientID)
			throws UnsupportedEncodingException {
		String base64ClientID = null;
		byte[] encoded = null;
		encoded = Base64.encodeBase64(clientID.getBytes("UTF-8"));
		base64ClientID = new String(encoded, "UTF-8");
		return base64ClientID;
	}

}
