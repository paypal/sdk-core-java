[![Build Status](https://travis-ci.org/paypal/sdk-core-java.svg?branch=master)](https://travis-ci.org/paypal/sdk-core-java) [![Coverage Status](https://coveralls.io/repos/github/paypal/sdk-core-java/badge.svg?branch=master)](https://coveralls.io/github/paypal/sdk-core-java?branch=master)

This repository contains core files for all Java sdks.

## Prerequisites:
*	Java JDK-1.5 or higher
*	Apache Maven 3 or higher

## To build this application:
*	Run 'mvn install' to build jar file.

## Core Testing:
*	Run 'mvn test' to run the test cases for all the core classes.
*	Test reports are generated in testReport folder.

## Core Logging:
*	For logging - java.util.logging has been used. To change the default configuration, edit the logging.properties file in 'jre/lib' folder under your JDK root.		  

		  
## Core Configuration:
The core uses .properties format configuration file. Sample of this file is at 'src/test/resources/'. You can use the 'sdk_config.properties' configuration file to configure

*	Mode is specified using the parameter name 'mode' with values 'sandbox' or 'live', if specified 'service.EndPoint' parameter is not required and the SDK choses the sandbox or live endpoints automatically.

*	(Multiple) API account credentials, by appending a '.' (dot) character and the service name to 'service.EndPoint' parameter.

*	HTTP connection parameters, if certain connection parameters are not specified, the SDK will assume defaults for them.

*	Service configuration.

## Credentials update
*NOTE*: This only applies to classic API SDKs.

*   [Get the new certificate](https://developer.paypal.com/docs/classic/api/apiCredentials/#renewing-an-api-certificate)
*   [Encrypt the new certificate](https://developer.paypal.com/docs/classic/api/apiCredentials/#encrypting-your-certificate)
*   Check [the encrypted certificate file's path in your config](https://github.com/paypal/sdk-core-java/blob/master/src/test/resources/sdk_config.properties#L12) and update if necessary.

## OpenID Connect Integration
   * Redirect your buyer to `Authorization.getRedirectUrl(redirectURI, scope, configurationMap);` to obtain authorization.
   * Capture the authorization code that is available as a query parameter (`code`) in the redirect url
   * Exchange the authorization code for a access token, refresh token, id token combo

```java
    Map<String, String> configurationMap = new HashMap<String, String>();
    configurationMap.put("clientId", "...");
    configurationMap.put("clientSecret", "...");
    configurationMap.put("service.EndPoint", "https://api.paypal.com/");
    APIContext apiContext = new APIContext();
    apiContext.setConfigurationMap(configurationMap);
    ...
    CreateFromAuthorizationCodeParameters param = new CreateFromAuthorizationCodeParameters();
    param.setCode(code);
    Tokeninfo info = Tokeninfo.createFromAuthorizationCode(apiContext, param);
    String accessToken = info.getAccessToken();
```
   * The access token is valid for a predefined duration and can be used for seamless XO or for retrieving user information

```java
    Map<String, String> configurationMap = new HashMap<String, String>();
    configurationMap.put("clientId", "...");
    configurationMap.put("clientSecret", "...");
    configurationMap.put("service.EndPoint", "https://api.paypal.com/");
    APIContext apiContext = new APIContext();
    apiContext.setConfigurationMap(configurationMap);
    ...
    Tokeninfo info = new Tokeninfo();
    info.setRefreshToken("refreshToken");
    UserinfoParameters param = new UserinfoParameters();
    param.setAccessToken(info.getAccessToken());
    Userinfo userInfo = Userinfo.userinfo(apiContext, param);
```
   * If the access token has expired, you can obtain a new access token using the refresh token from the 3'rd step.

```java
    Map<String, String> configurationMap = new HashMap<String, String>();
    configurationMap.put("clientId", "...");
    configurationMap.put("clientSecret", "...");
    configurationMap.put("service.EndPoint", "https://api.paypal.com/");
    APIContext apiContext = new APIContext();
    apiContext.setConfigurationMap(configurationMap);
    ...
    CreateFromRefreshTokenParameters param = new CreateFromRefreshTokenParameters();
    param.setScope("openid"); // Optional
    Tokeninfo info = new Tokeninfo // Create Token info object; setting the refresh token
    info.setRefreshToken("refreshToken");

    info.createFromRefreshToken(apiContext, param);
```

License
-------
Code released under [SDK LICENSE](LICENSE)

Contributions
-------------
Pull requests and new issues are welcome. See [CONTRIBUTING.md](CONTRIBUTING.md) for details.