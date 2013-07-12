package com.paypal.core.rest;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RESTConfigurationTestCase {

	@Test()
	public void testRESTConfiguration() {
		try {
			Map<String, String> configurationMap = new HashMap<String, String>();
			configurationMap.put("service.EndPoint", "https://localhost.sandbox.paypal.com");
			RESTAPICallPreHandler restConfiguration = new RESTAPICallPreHandler(configurationMap);
			restConfiguration.setResourcePath("/a/b/c");
			URL url = restConfiguration.getBaseURL();
			Assert.assertEquals(true, url.toString().endsWith("/"));
		} catch (MalformedURLException e) {
			Assert.fail();
		} 
	}

	@Test(dependsOnMethods = { "testRESTConfiguration" })
	public void testRESTHeaderConfiguration() {
			Map<String, String> configurationMap = new HashMap<String, String>();
			configurationMap.put("service.EndPoint", "https://localhost.sandbox.paypal.com");
			RESTAPICallPreHandler restConfiguration = new RESTAPICallPreHandler(configurationMap);
			restConfiguration.setResourcePath("/a/b/c");
			Map<String, String> headers = restConfiguration.getHeaderMap();
			Assert.assertEquals(headers.size() != 0, true);
			String header = headers.get("User-Agent");
			String[] hdrs = header.split("\\(");
			hdrs = hdrs[1].split(";");
			Assert.assertEquals(hdrs.length == 4, true);
	}

	@Test(dependsOnMethods = { "testRESTHeaderConfiguration" })
	public void testRESTConfigurationURL() {
		try {
			Map<String, String> configurationMap = new HashMap<String, String>();
			configurationMap.put("service.EndPoint", "https://localhost.sandbox.paypal.com");
			RESTAPICallPreHandler restConfiguration = new RESTAPICallPreHandler(configurationMap);
			restConfiguration.setResourcePath("/a/b/c");
			String urlString = "https://sample.com";
			restConfiguration.setUrl(urlString);
			URL returnURL = restConfiguration.getBaseURL();
			Assert.assertEquals(true, returnURL.toString().endsWith("/"));
		} catch (MalformedURLException e) {
			Assert.fail();
		}
	}
}
