/*
 * Copyright 2005 PayPal, Inc. All Rights Reserved.
 */

package com.paypal.sdk.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


import com.paypal.core.codec.binary.Base64;
import com.paypal.sdk.exceptions.OAuthException;

public class OAuthSignature {

	private static final String PARAM_DELIMETER = "&";
	private static final String PARAM_SEPERATOR = "=";
	private static final String ENCODING = "US-ASCII";
	private static final String SIGNATURE_METHOD = "HMAC-SHA1";
	private static final String SIGNATURE_ALGORITHM = "HmacSHA1";
	public static final String OAUTH_VERSION = "1.0";
	private String consumerKey;
	private String consumerSecret;
	private String token;
	private String tokenSecret;
	private String requestURI;
	private String timestamp;
	private String httpMethod;
	private List<Parameter> queryParams;

	public enum HTTPMethod {
		GET, HEAD, POST, PUT, UPDATE
	}

	/**
	 * Default Constructor
	 * 
	 * @param consumerKey
	 *            - Consumer key shared between PayPal and Consumer (OAuth
	 *            consumer)
	 * @param consumerSecret
	 *            - Secret shared between PayPal and Consumer (OAuth consumer)
	 */
	public OAuthSignature(String consumerKey, String consumerSecret) {
		this.queryParams = new ArrayList<Parameter>();
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.httpMethod = "POST";
	}

	/**
	 * Sets Token to be used to generate signature.
	 * 
	 * @param token
	 *            - String version of Token. The token could be Access or
	 *            Request
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Adds Parameter. Parameter could be part of URL, POST data.
	 * 
	 * @param name
	 *            parameter name with no URL encoding applied
	 * @param value
	 *            parameter value with no URL encoding applied
	 */
	public void addParameter(String name, String value) {
		queryParams.add(new Parameter(name, value));
	}

	/**
	 * Sets Token secret as received in Token response.
	 * 
	 * @param secret
	 *            byte array of token secret
	 */
	public void setTokenSecret(String secret) {
		this.tokenSecret = secret;
	}

	/**
	 * Sets URI for signature computation.
	 * 
	 * @param uri
	 *            - Script URI which will be normalized to
	 *            scheme://authority:port/path if not normalized already.
	 */
	public void setRequestURI(String uri) throws OAuthException {
		this.requestURI = normalizeURI(uri);
	}

	/**
	 * Sets time stamp for signature computation.
	 * 
	 * @param timestamp
	 *            - time stamp at which Token request sends.
	 */
	public void setTokenTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Sets HTTP Method
	 * 
	 * @param method
	 *            HTTP method used for sending OAuth request
	 */
	public void setHTTPMethod(HTTPMethod method) {
		switch (method) {
		case GET:
			httpMethod = "GET";
			break;
		case HEAD:
			httpMethod = "HEAD";
			break;
		case PUT:
			httpMethod = "PUT";
			break;
		case UPDATE:
			httpMethod = "UPDATE";
			break;
		default:
			httpMethod = "POST";
			break;
		}
	}

	/**
	 * Computes OAuth Signature as per OAuth specification using signature
	 * Method. using the specified encoding scheme {@code enc}.
	 * <p>
	 * 
	 * @return the Base64 encoded string.
	 * @throws OAuthException
	 *             if invalid arguments.
	 */
	public String computeV1Signature() throws OAuthException {
		validate(this.consumerKey, "API UserName");
		validate(this.consumerSecret, "API Password");
		validate(this.token, "Access Token");
		validate(this.tokenSecret, "Token Secret");
		validate(this.requestURI, "Request URI");
		validate(this.timestamp, "Timestamp");

		String signature = "";
		try {
			String key = PayPalURLEncoder.encode(consumerSecret, ENCODING);
			key += PARAM_DELIMETER;
			key += PayPalURLEncoder.encode(tokenSecret, ENCODING);

			List<Parameter> params = queryParams;

			params.add(new Parameter("oauth_consumer_key", this.consumerKey));
			params.add(new Parameter("oauth_version", OAUTH_VERSION));
			params.add(new Parameter("oauth_signature_method", SIGNATURE_METHOD));
			params.add(new Parameter("oauth_token", this.token));
			params.add(new Parameter("oauth_timestamp", this.timestamp));

			Collections.sort(params, new ParamComparator());

			String signatureBase = this.httpMethod + PARAM_DELIMETER;

			signatureBase += PayPalURLEncoder.encode(requestURI, ENCODING);
			signatureBase += PARAM_DELIMETER;

			String paramString = "";
			StringBuilder paramStringBuilder = new StringBuilder();
			Iterator<Parameter> it = params.iterator();
			while (it.hasNext()) {
				Parameter current = (Parameter) it.next();
				paramStringBuilder.append(current.getName())
						.append(PARAM_SEPERATOR).append(current.getValue());
				if (it.hasNext()) {
					paramStringBuilder.append(PARAM_DELIMETER);
				}
			}
			paramString = paramStringBuilder.toString();
			signatureBase += PayPalURLEncoder.encode(paramString, ENCODING);
			Mac hmac = Mac.getInstance(SIGNATURE_ALGORITHM);
			hmac.init(new SecretKeySpec(key.getBytes(ENCODING), hmac
					.getAlgorithm()));
			hmac.update(signatureBase.getBytes(ENCODING));
			byte[] digest = hmac.doFinal();
			Base64 b64Encoder = new Base64();
			signature = new String(b64Encoder.encode(digest), ENCODING);

		} catch (NoSuchAlgorithmException algoe) {
			throw new OAuthException(algoe.getMessage(), algoe);
		} catch (InvalidKeyException ke) {
			throw new OAuthException(ke.getMessage(), ke);
		} catch (UnsupportedEncodingException ee) {
			throw new OAuthException(ee.getMessage(), ee);
		}
		return signature;
	}

	/**
	 * Validate that the specified parameter is not null and not empty.
	 * 
	 * @param param
	 *            The parameter to validate
	 * @param name
	 *            The name of the parameter for the exception text.
	 * @throws OAuthException
	 * @throws OAuthException
	 *             If the parameter is not valid.
	 */
	private void validate(String param, String name) throws OAuthException {
		if ((param == null) || (param.length() == 0)) {
			throw new OAuthException("Value is required: " + name);
		}
	}

	/**
	 * verifyV1Signature verifies signature against computed signature.
	 * 
	 * @return true if signature verified otherwise false
	 * @throws OAuthException
	 *             in case there are any failures in signature computation.
	 */
	public boolean verifyV1Signature(String signature) throws OAuthException {
		String signatureComputed = computeV1Signature();
		return signatureComputed.equals(signature);
	}

	/**
	 * normalizeURI normalizes the given URI as per OAuth spec
	 * 
	 * @param uri
	 * @return normalized URI. URI normalized to scheme://authority:port/path
	 * @throws OAuthException
	 */
	private String normalizeURI(String uri) throws OAuthException {
		String normalizedURI = "", port = "", scheme = "", path = "", authority = "";
		int i, j, k;

		try {
			i = uri.indexOf(':');
			if (i == -1) {
				throw new OAuthException("Invalid URI.");
			} else {
				scheme = uri.substring(0, i);
			}
			// find next : in URL
			j = uri.indexOf(":", i + 2);
			if (j != -1) {
				// port has specified in URI
				authority = uri.substring(scheme.length() + 3, j);
				k = uri.indexOf("/", j);
				if (k != -1) {
					port = uri.substring(j + 1, k);
				} else {
					port = uri.substring(j + 1);
				}
			} else {
				// no port specified in uri
				k = uri.indexOf("/", scheme.length() + 3);
				if (k != -1) {
					authority = uri.substring(scheme.length() + 3, k);
				} else {
					authority = uri.substring(scheme.length() + 3);
				}
			}

			if (k != -1) {
				path = uri.substring(k);
			}
			normalizedURI = scheme.toLowerCase(Locale.US);
			normalizedURI += "://";
			normalizedURI += authority.toLowerCase(Locale.US);

			if (scheme != null && port.length() > 0) {
				if (scheme.equalsIgnoreCase("http")
						&& Integer.parseInt(port) != 80) {
					normalizedURI += ":";
					normalizedURI += port;
				} else if (scheme.equalsIgnoreCase("https")
						&& Integer.parseInt(port) != 443) {
					normalizedURI += ":";
					normalizedURI += port;
				}
			}

		} catch (NumberFormatException nfe) {
			throw new OAuthException("Invalid URI.", nfe);
		}
		normalizedURI += path;
		return normalizedURI;
	}

	/**
	 * Inner class for sorting Collection
	 * 
	 */
	private static class ParamComparator implements Comparator<Parameter>,
			Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8587372068875833370L;

		public int compare(Parameter x, Parameter y) {
			int retval = 0;
			if (x != null && y != null) {
				retval = ((Parameter) x).getName().compareTo(
						((Parameter) y).getName());
				// if parameter names are equal then compare parameter values.
				if (retval == 0) {
					retval = ((Parameter) x).getValue().compareTo(
							((Parameter) y).getValue());
				}
			}
			return retval;
		}
	}

	/**
	 * Inner class for representing Parameter
	 * 
	 */
	private static class Parameter {

		public Parameter(String name, String value) {
			this.mName = name;
			this.mValue = value;
		}

		public void setName(String name) {
			this.mName = name;
		}

		public void setValue(String val) {
			this.mValue = val;
		}

		public String getName() {
			return this.mName;
		}

		public String getValue() {
			return this.mValue;
		}

		private String mName;
		private String mValue;
	}

	/**
	 * Accepts the required parameters and Provides OAuth signature and
	 * TimeStamp.
	 * 
	 * @param apiUserName
	 *            API User name.
	 * @param apiPassword
	 *            API Password of user.
	 * @param accessToken
	 *            Obtained during Permission Request of token.
	 * @param tokenSecret
	 *            Obtained during Permission Request of token.
	 * @param httpMethod
	 *            HTTP Method (GET,POST etc.)
	 * @param scriptURI
	 *            API Server End Point.
	 * @param queryParams
	 *            Extra 'name/value' parameters if required.
	 * @return {@link Map} of HTTPHeaders
	 */
	public static Map getAuthHeader(String apiUserName, String apiPassword,
			String accessToken, String tokenSecret, HTTPMethod httpMethod,
			String scriptURI, Map queryParams) throws OAuthException {

		Map headers = new HashMap();
		String consumerKey = apiUserName;
		String consumerSecretStr = apiPassword;
		String time = String.valueOf(System.currentTimeMillis() / 1000);

		OAuthSignature oauth = new OAuthSignature(consumerKey,
				consumerSecretStr);
		if (HTTPMethod.GET.equals(httpMethod) && queryParams != null) {
			Iterator itr = queryParams.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry param = (Map.Entry) itr.next();
				String key = (String) param.getKey();
				String value = (String) param.getValue();
				oauth.addParameter(key, value);
			}
		}
		oauth.setToken(accessToken);
		oauth.setTokenSecret(tokenSecret);
		oauth.setHTTPMethod(httpMethod);
		oauth.setTokenTimestamp(time);
		oauth.setRequestURI(scriptURI);
		// Compute Signature
		String sig = oauth.computeV1Signature();

		headers.put("Signature", sig);
		headers.put("TimeStamp", time);
		return headers;

	}

	/**
	 * Computes the value of the X_PP_AUTHORIZATION header
	 * 
	 * @param apiUserName
	 *            API User name.
	 * @param apiPassword
	 *            API Password of user.
	 * @param accessToken
	 *            Obtained during Permission Request of token.
	 * @param tokenSecret
	 *            Obtained during Permission Request of token.
	 * @param httpMethod
	 *            HTTP Method (GET,POST etc.)
	 * @param scriptURI
	 *            API Server End Point.
	 * @param queryParams
	 *            Extra 'name/value' parameters if required.
	 * @return Auth String
	 * 
	 * @throws OAuthException
	 */
	public static String getFullAuthString(String apiUserName,
			String apiPassword, String accessToken, String tokenSecret,
			HTTPMethod httpMethod, String scriptURI, Map queryParams)
			throws OAuthException {

		Map headers = getAuthHeader(apiUserName, apiPassword, accessToken,
				tokenSecret, httpMethod, scriptURI, queryParams);
		return "token=" + accessToken + ",signature="
				+ headers.get("Signature") + ",timestamp="
				+ headers.get("TimeStamp");

	}
}
