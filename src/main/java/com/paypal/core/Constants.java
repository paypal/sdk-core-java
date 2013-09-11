package com.paypal.core;

public final class Constants {
	
	private Constants() {}

	// General Constants
	public static final String ENCODING_FORMAT = "UTF-8";
	public static final String EMPTY_STRING = "";
	public static final String ACCOUNT_PREFIX = "acct";

	public static final String PAYLOAD_FORMAT_SOAP = "SOAP";
	public static final String PAYLOAD_FORMAT_NVP = "NV";
	public static final String SDK_NAME = "";
	public static final String SDK_VERSION = "";

	// Default SDK configuration file name
	public static final String DEFAULT_CONFIGURATION_FILE = "sdk_config.properties";

	// HTTP Header Constants

	// HTTP Content-Type Header
	public static final String HTTP_CONTENT_TYPE_HEADER = "Content-Type";

	// HTTP Accept Header
	public static final String HTTP_ACCEPT_HEADER = "Accept";

	// PayPal Security UserId Header
	public static final String PAYPAL_SECURITY_USERID_HEADER = "X-PAYPAL-SECURITY-USERID";

	// PayPal Security Password Header
	public static final String PAYPAL_SECURITY_PASSWORD_HEADER = "X-PAYPAL-SECURITY-PASSWORD";

	// PayPal Security Signature Header
	public static final String PAYPAL_SECURITY_SIGNATURE_HEADER = "X-PAYPAL-SECURITY-SIGNATURE";

	// PayPal Platform Authorization Header
	public static final String PAYPAL_AUTHORIZATION_PLATFORM_HEADER = "X-PAYPAL-AUTHORIZATION";

	// PayPal Merchant Authorization Header
	public static final String PAYPAL_AUTHORIZATION_MERCHANT_HEADER = "X-PP-AUTHORIZATION";

	// PayPal Application ID Header
	public static final String PAYPAL_APPLICATION_ID_HEADER = "X-PAYPAL-APPLICATION-ID";

	// PayPal Request Data Header
	public static final String PAYPAL_REQUEST_DATA_FORMAT_HEADER = "X-PAYPAL-REQUEST-DATA-FORMAT";

	// PayPal Request Data Header
	public static final String PAYPAL_RESPONSE_DATA_FORMAT_HEADER = "X-PAYPAL-RESPONSE-DATA-FORMAT";

	// PayPal Request Source Header
	public static final String PAYPAL_REQUEST_SOURCE_HEADER = "X-PAYPAL-REQUEST-SOURCE";

	// PayPal Device IP Address Header
	public static final String PAYPAL_DEVICE_IPADDRESS_HEADER = "X-PAYPAL-DEVICE-IPADDRESS";

	// PayPal Sandbox Email Address for AA Header
	public static final String PAYPAL_SANDBOX_EMAIL_ADDRESS_HEADER = "X-PAYPAL-SANDBOX-EMAIL-ADDRESS";

	// Constants key defined for configuration options in application properties
	// End point
	public static final String ENDPOINT = "service.EndPoint";
	
	// OAuth End point
	public static final String OAUTH_ENDPOINT = "oauth.EndPoint";

	// Service Redirect Endpoint
	public static final String SERVICE_REDIRECT_ENDPOINT = "service.RedirectURL";

	// Service DevCentral Endpoint
	public static final String SERVICE_DEVCENTRAL_ENDPOINT = "service.DevCentralURL";

	// Use Google App Engine
	public static final String GOOGLE_APP_ENGINE = "http.GoogleAppEngine";

	// Use HTTP Proxy
	public static final String USE_HTTP_PROXY = "http.UseProxy";

	// HTTP Proxy host
	public static final String HTTP_PROXY_HOST = "http.ProxyHost";

	// HTTP Proxy port
	public static final String HTTP_PROXY_PORT = "http.ProxyPort";

	// HTTP Proxy username
	public static final String HTTP_PROXY_USERNAME = "http.ProxyUserName";

	// HTTP Proxy password
	public static final String HTTP_PROXY_PASSWORD = "http.ProxyPassword";

	// HTTP Connection Timeout
	public static final String HTTP_CONNECTION_TIMEOUT = "http.ConnectionTimeOut";

	// HTTP Connection Retry
	public static final String HTTP_CONNECTION_RETRY = "http.Retry";

	// HTTP Read timeout
	public static final String HTTP_CONNECTION_READ_TIMEOUT = "http.ReadTimeOut";

	// HTTP Max Connections
	public static final String HTTP_CONNECTION_MAX_CONNECTION = "http.MaxConnection";

	// HTTP Device IP Address Key
	public static final String DEVICE_IP_ADDRESS = "http.IPAddress";

	// Credential Username suffix
	public static final String CREDENTIAL_USERNAME_SUFFIX = ".UserName";

	// Credential Password suffix
	public static final String CREDENTIAL_PASSWORD_SUFFIX = ".Password";

	// Credential Application ID
	public static final String CREDENTIAL_APPLICATIONID_SUFFIX = ".AppId";

	// Credential Subject
	public static final String CREDENTIAL_SUBJECT_SUFFIX = ".Subject";

	// Credential Signature
	public static final String CREDENTIAL_SIGNATURE_SUFFIX = ".Signature";

	// Credential Certificate Path
	public static final String CREDENTIAL_CERTPATH_SUFFIX = ".CertPath";

	// Credential Certificate Key
	public static final String CREDENTIAL_CERTKEY_SUFFIX = ".CertKey";

	// Sandbox Email Address Key
	public static final String SANDBOX_EMAIL_ADDRESS = "sandbox.EmailAddress";

	// HTTP Configurations Defaults
	// HTTP Method Default
	public static final String HTTP_CONFIG_DEFAULT_HTTP_METHOD = "POST";

	// HTTP Content Type Default
	public static final String HTTP_CONFIG_DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

	// HTTP Content Type JSON
	public static final String HTTP_CONTENT_TYPE_JSON = "application/json";

	// IPN endpoint property name
	public static final String IPN_ENDPOINT = "service.IPNEndpoint";

	// Platform Sandbox Endpoint
	public static final String PLATFORM_SANDBOX_ENDPOINT = "https://svcs.sandbox.paypal.com/";

	// Platform Live Endpoint
	public static final String PLATFORM_LIVE_ENDPOINT = "https://svcs.paypal.com/";

	// IPN Sandbox Endpoint
	public static final String IPN_SANDBOX_ENDPOINT = "https://www.sandbox.paypal.com/cgi-bin/webscr";

	// IPN Live Endpoint
	public static final String IPN_LIVE_ENDPOINT = "https://ipnpb.paypal.com/cgi-bin/webscr";

	// Merchant Sandbox Endpoint Signature
	public static final String MERCHANT_SANDBOX_SIGNATURE_ENDPOINT = "https://api-3t.sandbox.paypal.com/2.0";

	// Merchant Live Endpoint Signature
	public static final String MERCHANT_LIVE_SIGNATURE_ENDPOINT = "https://api-3t.paypal.com/2.0";

	// Merchant Sandbox Endpoint Certificate
	public static final String MERCHANT_SANDBOX_CERTIFICATE_ENDPOINT = "https://api.sandbox.paypal.com/2.0";

	// Merchant Live Endpoint Certificate
	public static final String MERCHANT_LIVE_CERTIFICATE_ENDPOINT = "https://api.paypal.com/2.0";

	// REST Sandbox Endpoint
	public static final String REST_SANDBOX_ENDPOINT = "https://api.sandbox.paypal.com/";

	// REST Live Endpoint
	public static final String REST_LIVE_ENDPOINT = "https://api.paypal.com/";

	// Mode(sandbox/live)
	public static final String MODE = "mode";

	// SANDBOX Mode
	public static final String SANDBOX = "sandbox";

	// LIVE Mode
	public static final String LIVE = "live";

	// Open Id redirect URI
	public static final String OPENID_REDIRECT_URI = "openid.RedirectUri";

	// Open Id redirect URI Constant Live
	public static final String OPENID_REDIRECT_URI_CONSTANT_LIVE = "https://www.paypal.com/webapps/auth/protocol/openidconnect";
	
	// Open Id redirect URI Constant Sandbox
	public static final String OPENID_REDIRECT_URI_CONSTANT_SANDBOX = "https://www.sanbox.paypal.com/webapps/auth/protocol/openidconnect";

	// Client ID
	public static final String CLIENT_ID = "clientId";

	// Client Secret
	public static final String CLIENT_SECRET = "clientSecret";

}
