package com.paypal.core.rest;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RESTUtilTest {

	@Test()
	public void testFormatURIPathForNull() {
		String nullString = RESTUtil.formatURIPath((String) null,
				(Object[]) null);
		Assert.assertNull(nullString);
	}

	@Test(dependsOnMethods = { "testFormatURIPathForNull" })
	public void testFormatURIPathNoPattern() {
		String pattern = "/a/b/c";
		String uriPath = RESTUtil.formatURIPath(pattern, (Object[]) null);
		Assert.assertEquals(uriPath, pattern);
	}

	@Test(dependsOnMethods = { "testFormatURIPathNoPattern" })
	public void testFormatURIPathNoQS() {
		String pattern = "/a/b/{0}";
		Object[] parameters = new Object[] { "replace" };
		String uriPath = RESTUtil.formatURIPath(pattern, parameters);
		Assert.assertEquals(uriPath, "/a/b/replace");
	}

	@Test(dependsOnMethods = { "testFormatURIPathNoQS" })
	public void testFormatURIPath() {
		String pattern = "/a/b/{0}?name={1}";
		Object[] parameters = new Object[] { "replace", "nameValue" };
		String uriPath = RESTUtil.formatURIPath(pattern, parameters);
		Assert.assertEquals(uriPath, "/a/b/replace?name=nameValue");
	}

	@Test(dependsOnMethods = { "testFormatURIPath" })
	public void testFormatURIPathWithNull() {
		String pattern = "/a/b/{0}?name={1}&age={2}";
		Object[] parameters = new Object[] { "replace", "nameValue", null };
		String uriPath = RESTUtil.formatURIPath(pattern, parameters);
		Assert.assertEquals(uriPath, "/a/b/replace?name=nameValue");
	}

	@Test(dependsOnMethods = { "testFormatURIPathWithNull" })
	public void testFormatURIPathWithEmpty() {
		String pattern = "/a/b/{0}?name={1}&age=";
		Object[] parameters = new Object[] { "replace", "nameValue", null };
		String uriPath = RESTUtil.formatURIPath(pattern, parameters);
		Assert.assertEquals(uriPath, "/a/b/replace?name=nameValue");
	}

	@Test(dependsOnMethods = { "testFormatURIPathWithEmpty" })
	public void testFormatURIPathTwoQS() {
		String pattern = "/a/b/{0}?name={1}&age={2}";
		Object[] parameters = new Object[] { "replace", "nameValue", "1" };
		String uriPath = RESTUtil.formatURIPath(pattern, parameters);
		Assert.assertEquals(uriPath, "/a/b/replace?name=nameValue&age=1");
	}

	@Test(dependsOnMethods = { "testFormatURIPathTwoQS" })
	public void testFormatURIPathMap() throws PayPalRESTException {
		String pattern = "/a/b/{first}/{second}";
		Map<String, String> pathParameters = new HashMap<String, String>();
		pathParameters.put("first", "value1");
		pathParameters.put("second", "value2");
		String uriPath = RESTUtil.formatURIPath(pattern, pathParameters);
		Assert.assertEquals(uriPath, "/a/b/value1/value2");
	}

	@Test(dependsOnMethods = { "testFormatURIPathMap" })
	public void testFormatURIPathMapTraillingSlash() throws PayPalRESTException {
		String pattern = "/a/b/{first}/{second}/";
		Map<String, String> pathParameters = new HashMap<String, String>();
		pathParameters.put("first", "value1");
		pathParameters.put("second", "value2");
		String uriPath = RESTUtil.formatURIPath(pattern, pathParameters);
		Assert.assertEquals(uriPath, "/a/b/value1/value2/");
	}

	@Test(dependsOnMethods = { "testFormatURIPathMapTraillingSlash" })
	public void testFormatURIPathMapNullMap() throws PayPalRESTException {
		String pattern = "/a/b/first/second";
		String uriPath = RESTUtil.formatURIPath(pattern,
				(Map<String, String>) null);
		Assert.assertEquals(uriPath, "/a/b/first/second");
	}

	@Test(dependsOnMethods = { "testFormatURIPathMapNullMap" })
	public void testFormatURIPathMapIncorrectMap() throws PayPalRESTException {
		String pattern = "/a/b/first/second";
		Map<String, String> pathParameters = new HashMap<String, String>();
		pathParameters.put("invalid1", "value1");
		pathParameters.put("invalid2", "value2");
		String uriPath = RESTUtil.formatURIPath(pattern, pathParameters);
		Assert.assertEquals(uriPath, "/a/b/first/second");
	}

	@Test(dependsOnMethods = { "testFormatURIPathMapIncorrectMap" }, expectedExceptions = PayPalRESTException.class)
	public void testFormatURIPathMapInsufficientMap()
			throws PayPalRESTException {
		String pattern = "/a/b/{first}/{second}";
		Map<String, String> pathParameters = new HashMap<String, String>();
		pathParameters.put("first", "value1");
		RESTUtil.formatURIPath(pattern, pathParameters);
	}

	@Test(dependsOnMethods = { "testFormatURIPathMapInsufficientMap" })
	public void testFormatURIPathMapNullQueryMap() throws PayPalRESTException {
		String pattern = "/a/b/{first}/{second}";
		Map<String, String> pathParameters = new HashMap<String, String>();
		pathParameters.put("first", "value1");
		pathParameters.put("second", "value2");
		Map<String, String> queryParameters = null;
		String uriPath = RESTUtil.formatURIPath(pattern, pathParameters,
				queryParameters);
		Assert.assertEquals(uriPath, "/a/b/value1/value2");
	}

	@Test(dependsOnMethods = { "testFormatURIPathMapNullQueryMap" })
	public void testFormatURIPathMapEmptyQueryMap() throws PayPalRESTException {
		String pattern = "/a/b/{first}/{second}";
		Map<String, String> pathParameters = new HashMap<String, String>();
		pathParameters.put("first", "value1");
		pathParameters.put("second", "value2");
		Map<String, String> queryParameters = new HashMap<String, String>();
		String uriPath = RESTUtil.formatURIPath(pattern, pathParameters,
				queryParameters);
		Assert.assertEquals(uriPath, "/a/b/value1/value2");
	}

	@Test(dependsOnMethods = { "testFormatURIPathMapEmptyQueryMap" })
	public void testFormatURIPathMapQueryMap() throws PayPalRESTException {
		String pattern = "/a/b/first/second";
		Map<String, String> pathParameters = new HashMap<String, String>();
		Map<String, String> queryParameters = new HashMap<String, String>();
		queryParameters.put("query1", "value1");
		queryParameters.put("query2", "value2");
		String uriPath = RESTUtil.formatURIPath(pattern, pathParameters,
				queryParameters);
		Assert.assertEquals(uriPath,
				"/a/b/first/second?query1=value1&query2=value2&");
	}

	@Test(dependsOnMethods = { "testFormatURIPathMapQueryMap" })
	public void testFormatURIPathMapQueryMapQueryURIPath()
			throws PayPalRESTException {
		String pattern = "/a/b/first/second?";
		Map<String, String> pathParameters = new HashMap<String, String>();
		Map<String, String> queryParameters = new HashMap<String, String>();
		queryParameters.put("query1", "value1");
		queryParameters.put("query2", "value2");
		String uriPath = RESTUtil.formatURIPath(pattern, pathParameters,
				queryParameters);
		Assert.assertEquals(uriPath,
				"/a/b/first/second?query1=value1&query2=value2&");
	}

	@Test(dependsOnMethods = { "testFormatURIPathMapQueryMapQueryURIPath" })
	public void testFormatURIPathMapQueryMapQueryURIPathEncode()
			throws PayPalRESTException {
		String pattern = "/a/b/first/second";
		Map<String, String> pathParameters = new HashMap<String, String>();
		Map<String, String> queryParameters = new HashMap<String, String>();
		queryParameters.put("query1", "value&1");
		queryParameters.put("query2", "value2");
		String uriPath = RESTUtil.formatURIPath(pattern, pathParameters,
				queryParameters);
		Assert.assertEquals(uriPath,
				"/a/b/first/second?query1=value%261&query2=value2&");
	}

	@Test(dependsOnMethods = { "testFormatURIPathMapQueryMapQueryURIPathEncode" })
	public void testFormatURIPathMapQueryMapQueryValueURIPath()
			throws PayPalRESTException {
		String pattern = "/a/b/first/second?alreadypresent=value";
		Map<String, String> pathParameters = new HashMap<String, String>();
		Map<String, String> queryParameters = new HashMap<String, String>();
		queryParameters.put("query1", "value1");
		queryParameters.put("query2", "value2");
		String uriPath = RESTUtil.formatURIPath(pattern, pathParameters,
				queryParameters);
		Assert.assertEquals(uriPath,
				"/a/b/first/second?alreadypresent=value&query1=value1&query2=value2&");
	}

}
