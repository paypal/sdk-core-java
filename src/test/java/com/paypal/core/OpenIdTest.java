package com.paypal.core;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.sdk.openidconnect.CreateFromAuthorizationCodeParameters;
import com.paypal.sdk.openidconnect.CreateFromRefreshTokenParameters;
import com.paypal.sdk.openidconnect.Session;
import com.paypal.sdk.openidconnect.Tokeninfo;
import com.paypal.sdk.openidconnect.Userinfo;
import com.paypal.sdk.openidconnect.UserinfoParameters;

public class OpenIdTest {

	private static final Logger logger = Logger.getLogger(OpenIdTest.class);

	private Tokeninfo info;

	Map<String, String> configurationMap = new HashMap<String, String>();

	public OpenIdTest() {
		 /*configurationMap.put("clientId", "");
		 configurationMap.put("clientSecret", "");
		 configurationMap.put("mode", "live");*/
	}

	@Test(enabled = false)
	public void testCreateFromAuthorizationCodeDynamic()
			throws PayPalRESTException, UnsupportedEncodingException {
		CreateFromAuthorizationCodeParameters param = new CreateFromAuthorizationCodeParameters();
		param.setCode("xxxxx");
		APIContext apiContext = new APIContext();
		apiContext.setConfigurationMap(configurationMap);
		info = Tokeninfo.createFromAuthorizationCode(apiContext, param);
		logger.info("Generated Access Token : " + info.getAccessToken());
		logger.info("Generated Refrest Token: " + info.getRefreshToken());
	}

	@Test(dependsOnMethods = { "testCreateFromAuthorizationCodeDynamic" }, enabled = false)
	public void testCreateFromRefreshTokenDynamic() throws PayPalRESTException {
		CreateFromRefreshTokenParameters param = new CreateFromRefreshTokenParameters();
		APIContext apiContext = new APIContext();
		apiContext.setConfigurationMap(configurationMap);
		info = info.createFromRefreshToken(apiContext, param);
		logger.info("Regenerated Access Token: " + info.getAccessToken());
		logger.info("Refresh Token: " + info.getRefreshToken());
	}

	@Test(dependsOnMethods = { "testCreateFromRefreshTokenDynamic" }, enabled = false)
	public void testUserinfoDynamic() throws PayPalRESTException {
		UserinfoParameters param = new UserinfoParameters();
		param.setAccessToken(info.getAccessToken());
		APIContext apiContext = new APIContext();
		apiContext.setConfigurationMap(configurationMap);
		Userinfo userInfo = Userinfo.getUserinfo(apiContext, param);
		logger.info("User Info Email: " + userInfo.getEmail());
		logger.info("User Info Account Type: " + userInfo.getAccountType());
		logger.info("User Info Name: " + userInfo.getGivenName());
	}

	@Test()
	public void testAuthorizationURL() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("openid.RedirectUri",
				"https://www.paypal.com/webapps/auth/protocol/openidconnect");
		m.put("clientId", "ANdfsalkoiarT");
		List<String> l = new ArrayList<String>();
		l.add("openid");
		l.add("profile");
		APIContext apiContext = new APIContext();
		apiContext.setConfigurationMap(m);
		String redirectURL = Session.getRedirectURL("http://google.com", l,
				apiContext);
		logger.info("Redirect URL: " + redirectURL);
		Assert.assertEquals(
				redirectURL,
				"https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize?client_id=ANdfsalkoiarT&response_type=code&scope=openid+profile+&redirect_uri=http%3A%2F%2Fgoogle.com");
	}

	@Test()
	public void testLogoutURL() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("openid.RedirectUri",
				"https://www.paypal.com/webapps/auth/protocol/openidconnect");
		APIContext apiContext = new APIContext();
		apiContext.setConfigurationMap(m);
		String logoutURL = Session.getLogoutUrl("http://google.com", "tokenId",
				apiContext);
		logger.info("Redirect URL: " + logoutURL);
		Assert.assertEquals(
				logoutURL,
				"https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/endsession?id_token=tokenId&redirect_uri=http%3A%2F%2Fgoogle.com&logout=true");
	}
}
