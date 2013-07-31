package com.paypal.core.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.paypal.core.ConfigManager;
import com.paypal.core.ConnectionManager;
import com.paypal.core.HttpConfiguration;
import com.paypal.core.HttpConnection;
import com.paypal.core.LoggingManager;
import com.paypal.core.SDKUtil;

/**
 * PayPalResource acts as a base class for REST enabled resources
 */
public abstract class PayPalResource {

	/**
	 * SDK ID used in User-Agent HTTP header
	 */
	public static final String SDK_ID = "rest-sdk-java";

	/**
	 * SDK Version used in User-Agent HTTP header
	 */
	public static final String SDK_VERSION = "0.7.1";

	/**
	 * Map used in dynamic configuration
	 */
	private static Map<String, String> configurationMap;

	/**
	 * Configuration enabled flag
	 */
	private static boolean configInitialized = false;

	/**
	 * Last request sent to Service
	 */
	private static final ThreadLocal<String> LASTREQUEST = new ThreadLocal<String>();

	/**
	 * Last response returned form Service
	 */
	private static final ThreadLocal<String> LASTRESPONSE = new ThreadLocal<String>();

	/**
	 * Initialize using InputStream(of a Properties file)
	 * 
	 * @param is
	 *            InputStream
	 * @throws PayPalRESTException
	 */
	public static void initConfig(InputStream inputStream)
			throws PayPalRESTException {
		try {
			Properties properties = new Properties();
			properties.load(inputStream);
			configurationMap = SDKUtil.constructMap(properties);
			configInitialized = true;
		} catch (IOException ioe) {
			LoggingManager.severe(PayPalResource.class, ioe.getMessage(), ioe);
			throw new PayPalRESTException(ioe.getMessage(), ioe);
		}

	}

	/**
	 * Initialize using a File(Properties file)
	 * 
	 * @param file
	 *            File object of a properties entity
	 * @throws PayPalRESTException
	 */
	public static void initConfig(File file) throws PayPalRESTException {
		try {
			if (!file.exists()) {
				throw new FileNotFoundException("File doesn't exist: "
						+ file.getAbsolutePath());
			}
			FileInputStream fis = new FileInputStream(file);
			initConfig(fis);
		} catch (IOException ioe) {
			LoggingManager.severe(PayPalResource.class, ioe.getMessage(), ioe);
			throw new PayPalRESTException(ioe.getMessage(), ioe);
		}

	}

	/**
	 * Initialize using Properties
	 * 
	 * @param properties
	 *            Properties object
	 */
	public static void initConfig(Properties properties) {
		configurationMap = SDKUtil.constructMap(properties);
		configInitialized = true;
	}

	/**
	 * Initialize to default properties
	 * 
	 * @throws PayPalRESTException
	 */
	private static void initializeToDefault() throws PayPalRESTException {
		configurationMap = SDKUtil.combineDefaultMap(ConfigManager
				.getInstance().getConfigurationMap());
	}

	/**
	 * Returns the last request sent to the Service
	 * 
	 * @return Last request sent to the server
	 */
	public static String getLastRequest() {
		return LASTREQUEST.get();
	}

	/**
	 * Returns the last response returned by the Service
	 * 
	 * @return Last response got from the Service
	 */
	public static String getLastResponse() {
		return LASTRESPONSE.get();
	}

	/**
	 * Configures and executes REST call: Supports JSON
	 * 
	 * @deprecated
	 * @param <T>
	 *            Response Type for de-serialization
	 * @param accessToken
	 *            AccessToken to be used for the call.
	 * @param httpMethod
	 *            Http Method verb
	 * @param resourcePath
	 *            Resource URI path
	 * @param payLoad
	 *            Payload to Service
	 * @param clazz
	 *            {@link Class} object used in De-serialization
	 * @return T
	 * @throws PayPalRESTException
	 */
	public static <T> T configureAndExecute(String accessToken,
			HttpMethod httpMethod, String resourcePath, String payLoad,
			Class<T> clazz) throws PayPalRESTException {
		return configureAndExecute(null, accessToken, httpMethod, resourcePath,
				null, payLoad, null, clazz);
	}

	/**
	 * Configures and executes REST call: Supports JSON
	 * 
	 * @param <T>
	 *            Response Type for de-serialization
	 * @param apiContext
	 *            {@link APIContext} to be used for the call.
	 * @param httpMethod
	 *            Http Method verb
	 * @param resource
	 *            Resource URI path
	 * @param payLoad
	 *            Payload to Service
	 * @param clazz
	 *            {@link Class} object used in De-serialization
	 * @return T
	 * @throws PayPalRESTException
	 */
	public static <T> T configureAndExecute(APIContext apiContext,
			HttpMethod httpMethod, String resourcePath, String payLoad,
			Class<T> clazz) throws PayPalRESTException {
		Map<String, String> cMap = null;
		String accessToken = null;
		String requestId = null;
		if (apiContext != null) {
			cMap = apiContext.getConfigurationMap();
			accessToken = apiContext.getAccessToken();
			requestId = apiContext.getRequestId();
		}
		return configureAndExecute(cMap, accessToken, httpMethod,
				resourcePath, null, payLoad, requestId, clazz);
	}

	/**
	 * Configures and executes REST call: Supports JSON
	 * 
	 * @param <T>
	 * @param apiContext
	 *            {@link APIContext} to be used for the call.
	 * @param httpMethod
	 *            Http Method verb
	 * @param resourcePath
	 *            Resource URI path
	 * @param headersMap
	 *            Optional headers Map
	 * @param payLoad
	 *            Payload to Service
	 * @param clazz
	 *            {@link Class} object used in De-serialization
	 * @return T
	 * @throws PayPalRESTException
	 */
	public static <T> T configureAndExecute(APIContext apiContext,
			HttpMethod httpMethod, String resourcePath,
			Map<String, String> headersMap, String payLoad, Class<T> clazz)
			throws PayPalRESTException {
		Map<String, String> cMap = null;
		if (apiContext != null) {
			cMap = apiContext.getConfigurationMap();
		}
		return configureAndExecute(cMap, null, httpMethod,
				resourcePath, headersMap, payLoad, null, clazz);
	}

	private static <T> T configureAndExecute(
			Map<String, String> configurationMap, String accessToken,
			HttpMethod httpMethod, String resourcePath,
			Map<String, String> headersMap, String payLoad, String requestId,
			Class<T> clazz) throws PayPalRESTException {
		T t = null;
		Map<String, String> cMap = null;
		if (configurationMap != null) {
			cMap = SDKUtil.combineDefaultMap(configurationMap);
		} else {
			if (!configInitialized) {
				initializeToDefault();
			}
			cMap = new HashMap<String, String>(
					PayPalResource.configurationMap);
		}
		RESTConfiguration restConfiguration = createRESTConfiguration(
				cMap, httpMethod, resourcePath, headersMap,
				accessToken, requestId);
		t = execute(restConfiguration, payLoad, resourcePath, clazz);
		return t;

	}

	/**
	 * Creates a {@link RESTConfiguration} based on configuration
	 * 
	 * @param httpMethod
	 *            {@link HttpMethod}
	 * @param resourcePath
	 *            Resource URI
	 * @param accessToken
	 *            Access Token
	 * @param requestId
	 *            Request Id
	 * @return
	 */
	private static RESTConfiguration createRESTConfiguration(
			Map<String, String> configurationMap, HttpMethod httpMethod,
			String resourcePath, Map<String, String> headersMap,
			String accessToken, String requestId) {
		RESTConfiguration restConfiguration = new RESTConfiguration(
				configurationMap, headersMap);
		restConfiguration.setHttpMethod(httpMethod);
		restConfiguration.setResourcePath(resourcePath);
		restConfiguration.setRequestId(requestId);
		restConfiguration.setAuthorizationToken(accessToken);
		return restConfiguration;
	}

	/**
	 * Execute the API call and return response
	 * 
	 * @param <T>
	 *            Type of the return object
	 * @param restConfiguration
	 *            {@link RESTConfiguration}
	 * @param payLoad
	 *            Payload
	 * @param resourcePath
	 *            Resource URI
	 * @param clazz
	 *            Class of the return object
	 * @return API response type object
	 * @throws PayPalRESTException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ClientActionRequiredException
	 * @throws HttpErrorException
	 * @throws InvalidResponseDataException
	 */
	private static <T> T execute(RESTConfiguration restConfiguration,
			String payLoad, String resourcePath, Class<T> clazz)
			throws PayPalRESTException {
		T t = null;
		ConnectionManager connectionManager;
		HttpConnection httpConnection;
		HttpConfiguration httpConfig;
		Map<String, String> headers;
		String responseString;
		try {

			// REST Headers
			headers = restConfiguration.getHeaders();

			// HTTPConfiguration Object
			httpConfig = restConfiguration.getHttpConfigurations();

			// HttpConnection Initialization
			connectionManager = ConnectionManager.getInstance();
			httpConnection = connectionManager.getConnection(httpConfig);
			httpConnection.createAndconfigureHttpConnection(httpConfig);

			LASTREQUEST.set(payLoad);
			responseString = httpConnection.execute(restConfiguration
					.getBaseURL().toURI().resolve(resourcePath).toString(),
					payLoad, headers);
			LASTRESPONSE.set(responseString);
			if (clazz != null) {
				t = JSONFormatter.fromJSON(responseString, clazz);
			}
		} catch (Exception e) {
			throw new PayPalRESTException(e.getMessage(), e);
		}
		return t;
	}

}
