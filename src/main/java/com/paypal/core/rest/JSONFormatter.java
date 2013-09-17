package com.paypal.core.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSONFormatter converts objects to JSON representation and vice-versa. This
 * class depends on Google's GSON library to do the transformation. This class
 * is not thread-safe.
 * 
 * @author kjayakumar
 * 
 */
public final class JSONFormatter {

	/*
	 * JSONFormatter is coupled to the stubs generated using the SDK generator.
	 * Since PayPal REST APIs support only JSON, this class is bound to the
	 * stubs for their json representation.
	 */
	private JSONFormatter() {
	}

	/**
	 * FieldNamingPolicy used by the underlying Gson library. Alter this
	 * property to set a fieldnamingpolicy other than
	 * LOWER_CASE_WITH_UNDERSCORES used by PayPal REST APIs
	 */
	private static FieldNamingPolicy FIELD_NAMING_POLICY = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

	/**
	 * Gson
	 */
	public static Gson GSON = new GsonBuilder().setPrettyPrinting()
			.setFieldNamingPolicy(FIELD_NAMING_POLICY).create();

	/**
	 * Set a format for gson FIELD_NAMING_POLICY. See {@link FieldNamingPolicy}
	 * 
	 * @param FIELD_NAMING_POLICY
	 */
	public static final void setFIELD_NAMING_POLICY(
			FieldNamingPolicy FIELD_NAMING_POLICY) {
		GSON = new GsonBuilder().setPrettyPrinting()
				.setFieldNamingPolicy(FIELD_NAMING_POLICY).create();
	}

	/**
	 * Converts a Raw Type to JSON String
	 * 
	 * @param <T>
	 *            Type to be converted
	 * @param t
	 *            Object of the type
	 * @return JSON representation
	 */
	public static <T> String toJSON(T t) {
		return GSON.toJson(t);
	}

	/**
	 * Converts a JSON String to object representation
	 * 
	 * @param <T>
	 *            Type to be converted
	 * @param responseString
	 *            JSON representation
	 * @param clazz
	 *            Target class
	 * @return Object of the target type
	 */
	public static <T> T fromJSON(String responseString, Class<T> clazz) {
		T t = null;
		if (clazz.isAssignableFrom(responseString.getClass())) {
			t = clazz.cast(responseString);
		} else {
			t = GSON.fromJson(responseString, clazz);
		}
		return t;
	}

}
