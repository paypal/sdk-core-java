package com.paypal.core.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.core.SDKVersion;

public class OAuthTokenCredentialTestCase {

	private static final Logger logger = Logger
			.getLogger(OAuthTokenCredentialTestCase.class);

	String clientID;
	String clientSecret;

	@BeforeClass
	public void beforeClass() {
		clientID = "EBWKjlELKMYqRNQ6sYvFo64FtaRLRR5BdHEESmha49TM";
		clientSecret = "EO422dn3gQLgDbuwqTjzrFgFtaRLRR5BdHEESmha49TM";
	}

	@Test(priority = 20)
	public void testGetAccessToken() throws PayPalRESTException {
		Map<String, String> configurationMap = new HashMap<String, String>();
		configurationMap.put("service.EndPoint",
				"https://api.sandbox.paypal.com");
		OAuthTokenCredential merchantTokenCredential = new OAuthTokenCredential(
				clientID, clientSecret, configurationMap);
		String accessToken = merchantTokenCredential.getAccessToken();
		logger.info("Generated Access Token = " + accessToken);
		Assert.assertEquals(true, accessToken.length() > 0);
	}

	@Test(dependsOnMethods = { "testGetAccessToken" })
	public void testErrorAccessToken() {
		try {
			Map<String, String> configurationMap = new HashMap<String, String>();
			configurationMap.put("service.EndPoint",
					"https://localhost.sandbox.paypal.com");
			OAuthTokenCredential merchantTokenCredential = new OAuthTokenCredential(
					clientID, clientSecret, configurationMap);
			merchantTokenCredential.getAccessToken();
		} catch (PayPalRESTException e) {
			Assert.assertEquals(true, e.getCause() instanceof IOException);
		}
	}

}
