package com.paypal.core.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.paypal.core.Constants;
import com.paypal.sdk.openidconnect.CreateFromAuthorizationCodeParameters;
import com.paypal.sdk.openidconnect.CreateFromRefreshTokenParameters;
import com.paypal.sdk.openidconnect.UserinfoParameters;

public final class RESTUtil {

	private RESTUtil() {
	}

	/**
	 * Formats the URI path for REST calls.
	 * 
	 * @param pattern
	 *            URI pattern with place holders for replacement strings
	 * @param parameters
	 *            Replacement objects
	 * @return Formatted URI path
	 */
	public static String formatURIPath(String pattern, Object[] parameters) {
		String formattedPath = null;
		Object[] finalParameters = null;
		if (pattern != null) {
			if (parameters != null
					&& parameters.length == 1
					&& parameters[0] instanceof CreateFromAuthorizationCodeParameters) {

				// Form a object array using the passed
				// CreateFromAuthorizationCodeParameters
				finalParameters = splitParameters(pattern,
						((CreateFromAuthorizationCodeParameters) parameters[0])
								.getContainerMap());
			} else if (parameters != null
					&& parameters.length == 1
					&& parameters[0] instanceof CreateFromRefreshTokenParameters) {

				// Form a object array using the passed
				// CreateFromRefreshTokenParameters
				finalParameters = splitParameters(pattern,
						((CreateFromRefreshTokenParameters) parameters[0])
								.getContainerMap());
			} else if (parameters != null && parameters.length == 1
					&& parameters[0] instanceof UserinfoParameters) {

				// Form a object array using the passed UserinfoParameters
				finalParameters = splitParameters(pattern,
						((UserinfoParameters) parameters[0]).getContainerMap());
			} else if (parameters != null && parameters.length == 1
					&& parameters[0] instanceof QueryParameters) {

				// Form a object array using the passed UserinfoParameters
				finalParameters = splitParameters(pattern,
						((QueryParameters) parameters[0]).getContainerMap());
			} else if (parameters != null && parameters.length == 1
					&& parameters[0] instanceof Map<?, ?>) {

				// Form a object array using the passed Map
				finalParameters = splitParameters(pattern,
						((Map<?, ?>) parameters[0]));
			} else {
				finalParameters = parameters;
			}

			// Perform a simple message formatting
			String fString = MessageFormat.format(pattern, finalParameters);

			// Process the resultant string for removing nulls
			formattedPath = removeNullsInQS(fString);
		}
		return formattedPath;
	}

	/**
	 * Remove null parameters from query string
	 * 
	 * @param fString
	 *            Formatted String
	 * @return Nulls removed query string
	 */
	private static String removeNullsInQS(String fString) {
		String formattedString = fString;
		if (fString != null && fString.length() != 0) {
			String[] parts = fString.split("\\?");

			// Process the query string part
			if (parts.length == 2) {
				String queryString = parts[1];
				String[] querys = queryString.split("&");
				if (querys.length > 0) {
					StringBuilder strBuilder = new StringBuilder();
					for (String query : querys) {
						String[] valueSplit = query.split("=");
						if (valueSplit.length == 2) {
							if ("null".equalsIgnoreCase(valueSplit[1].trim())) {
								continue;
							} else if ("".equals(valueSplit[1].trim())) {
								continue;
							} else {
								strBuilder.append(query).append("&");
							}
						} else if (valueSplit.length < 2) {
							continue;
						}
					}
					formattedString = (!strBuilder.toString().endsWith("&")) ? strBuilder
							.toString() : strBuilder.toString().substring(0,
							strBuilder.toString().length() - 1);
				}

				// append the query string delimiter
				formattedString = (parts[0].trim() + "?") + formattedString;
			}
		}
		return formattedString;
	}

	/**
	 * Split the URI and form a Object array using the query string and values
	 * in the provided map. The return object array is populated only if the map
	 * contains valid value for the query name. The object array contains null
	 * values if there is no value found in the map
	 * 
	 * @param pattern
	 *            URI pattern
	 * @param containerMap
	 *            Map containing the query name and value
	 * @return Object array
	 */
	private static Object[] splitParameters(String pattern,
			Map<?, ?> containerMap) {
		List<Object> objectList = new ArrayList<Object>();
		String[] query = pattern.split("\\?");
		if (query != null && query.length == 2 && query[1].contains("={")) {
			String[] queries = query[1].split("&");
			if (queries != null) {
				for (String q : queries) {
					String[] params = q.split("=");
					if (params != null && params.length == 2) {
						String key = params[0].trim();
						if (containerMap.containsKey(key)) {
							Object object = containerMap.get(key);
							try {
								objectList.add(URLEncoder.encode(
										(String) object, Constants.ENCODING_FORMAT));
							} catch (UnsupportedEncodingException e) {
								// Ignore
							}
						} else {
							objectList.add(null);
						}
					}
				}
			}
		}
		return objectList.toArray();
	}

}
