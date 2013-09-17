package com.paypal.core;

/**
 * 
 * Class contains http specific configuration parameters
 * 
 */
public class HttpConfiguration {

	/**
	 * Maximum retries on failure
	 */
	private int maxRetry;

	/**
	 * Use PROXY configuration
	 */
	private boolean proxySet;

	/**
	 * PROXY host
	 */
	private String proxyHost;

	/**
	 * PROXY port
	 */
	private int proxyPort;

	/**
	 * PROXY username
	 */
	private String proxyUserName;

	/**
	 * PROXY password
	 */
	private String proxyPassword;

	/**
	 * Connection read timeout
	 */
	private int readTimeout;

	/**
	 * Connection timeout
	 */
	private int connectionTimeout;

	/**
	 * Maximum HTTP connections
	 */
	private int maxHttpConnection;

	/**
	 * End point URL
	 */
	private String endPointUrl;

	/**
	 * Google App Engine (Use {@link GoogleAppEngineHttpConnection})
	 */
	private boolean googleAppEngine;

	/**
	 * Delay used for retry mechanism
	 */
	private int retryDelay;

	/**
	 * IP Address
	 */
	private String ipAddress;

	/**
	 * HTTP method, defaulted to HTTP POST
	 */
	private String httpMethod;

	/**
	 * HTTP Content Type value, defaulted to 'application/x-www-form-urlencoded'
	 * @deprecated Set Content-Type in HTTP Headers property of {@link BaseAPIContext}
	 */
	private String contentType;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public HttpConfiguration() {

		this.maxRetry = 2;

		this.proxySet = false;

		this.proxyHost = null;

		this.proxyPort = -1;

		this.proxyUserName = null;

		this.proxyPassword = null;

		this.readTimeout = 0;

		this.connectionTimeout = 0;

		this.maxHttpConnection = 10;

		this.endPointUrl = null;

		this.retryDelay = 1000;

		this.ipAddress = "127.0.0.1";

		this.httpMethod = Constants.HTTP_CONFIG_DEFAULT_HTTP_METHOD;

	}

	/**
	 * @return the proxyUserName
	 */
	public String getProxyUserName() {
		return proxyUserName;
	}

	/**
	 * Sets the proxyUserName
	 * 
	 * @param proxyUserName
	 */
	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}

	/**
	 * @return the proxyPassword
	 */
	public String getProxyPassword() {
		return proxyPassword;
	}

	/**
	 * Sets the proxyPassword
	 * 
	 * @param proxyPassword
	 */
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	/**
	 * @return the maxHttpConnection
	 */
	public int getMaxHttpConnection() {
		return maxHttpConnection;
	}

	/**
	 * Sets the maxHttpConnection
	 * 
	 * @param maxHttpConnection
	 */
	public void setMaxHttpConnection(int maxHttpConnection) {
		this.maxHttpConnection = maxHttpConnection;
	}

	/**
	 * @return the retryDelay
	 */
	public int getRetryDelay() {
		return retryDelay;
	}

	/**
	 * Sets the retryDelay
	 * 
	 * @param retryDelay
	 */
	public void setRetryDelay(int retryDelay) {
		this.retryDelay = retryDelay;
	}

	/**
	 * @return the endPointUrl
	 */
	public String getEndPointUrl() {
		return endPointUrl;
	}

	/**
	 * Sets the endPointUrl
	 * 
	 * @param endPointUrl
	 */
	public void setEndPointUrl(String endPointUrl) {
		this.endPointUrl = endPointUrl;
	}

	/**
	 * @return the maxRetry
	 */
	public int getMaxRetry() {
		return maxRetry;
	}

	/**
	 * Sets the maxRetry
	 * 
	 * @param maxRetry
	 */
	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	/**
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * Sets the proxyHost
	 * 
	 * @param proxyHost
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @return the proxyPort
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * Sets the proxyPort
	 * 
	 * @param proxyPort
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * @return the readTimeout
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * Sets the readTimeout
	 * 
	 * @param readTimeout
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * Sets the connectionTimeout
	 * 
	 * @param connectionTimeout
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return the proxySet
	 */
	public boolean isProxySet() {
		return proxySet;
	}

	/**
	 * Sets the proxySet
	 * 
	 * @param proxySet
	 */
	public void setProxySet(boolean proxySet) {
		this.proxySet = proxySet;
	}

	/**
	 * @return the googleAppEngine
	 */
	public boolean isGoogleAppEngine() {
		return googleAppEngine;
	}

	/**
	 * Sets the googleAppEngine
	 * 
	 * @param googleAppEngine
	 */
	public void setGoogleAppEngine(boolean googleAppEngine) {
		this.googleAppEngine = googleAppEngine;
	}

	/**
	 * @return the httpMethod
	 */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * @param httpMethod
	 *            the httpMethod to set
	 */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * @deprecated Set/Get Content-Type HTTP Header in {@link BaseAPIContext} HTTPHeaders parameter
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @deprecated Set/Get Content-Type HTTP Header in {@link BaseAPIContext} HTTPHeaders parameter
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
