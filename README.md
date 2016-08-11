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

Developers must configure the Java SDKs from PayPal with information such as API credentials, integration mode etc. 

### How to configure

The SDK can be configured via a static config file (useful for simple applications) or by dynamic configuration to your service object constructor (useful for larger applications that have complex configuration requirements or need to read account information from a database etc).

To use file based configuration, just create a file with the name 'sdk_config.properties', make sure the file is in class path. Use the default constructor to run with configuration from 'sdk_config.properties' found in classpath.
 ```java
 new PermissionsService();
 ```
If you want to use a dynamic configuration instead, 
  
Create a hash map with the required configuration parameters and pass the hashmap to your service object constructor. For example,
  ```java
   Map<String, String> customConfigurationMap = new HashMap<String, String>();
   customConfigurationMap.put("mode", "sandbox");
   customConfigurationMap.put("acct1.UserName", "jb-us-seller_api1.paypal.com"); 
   customConfigurationMap.put("acct1.Password", "WX4WTU3S8MY44S7F");
   .... 
   new PermissionsService(Map<String, String> customConfigurationMap);
        Or
   new PermissionsService(new File("/pathto/custom.properties"));
        Or
   new PermissionsService(new FileInputStream(new File("/pathto/custom.properties")));
        Or
   new PermissionsService("/pathto/custom.properties");
        Or
   new PermissionsService(Properties customProperties);
  ```
### List of configuration parameters

#### Service Configuration

* Integration mode 

PayPal provides live and a sandbox environments for API calls. The live environment moves real money while the sandbox environment allows you to test your application with mock money before you go live. You must set the mode property to either 'sandbox' or 'live'.

```properties
  mode=sandbox
```
#### API credentials

You can configure one or more API credentials using the acct* parameters. PayPal API calls can be authenticated using multiple methods

   * 3-token authentication, OR
   * client certificate authentication.

You can obtain your credentials by logging in to your PayPal account and navigating to the **Profile -> My Selling Tools -> API Access** page. Make sure to also go through the [Getting started guide](https://developer.paypal.com/webapps/developer/docs/classic/api/gs_PayPalAPIs/). If you are using the Adaptive APIs, you will need to pass in an [Application ID](https://developer.paypal.com/webapps/developer/docs/classic/api/gs_PayPalAPIs/#Credentials) as well.

**Sample 3-token configuration**

```properties
acct1.UserName = jb-us-seller_api1.paypal.com
acct1.Password = WX4WTU3S8MY44S7F
acct1.Signature = AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy
acct1.AppId=APP-80W284485P519543T
```

**Sample certificate configuration**
```properties
acct2.UserName = certuser_biz_api1.paypal.com
acct2.Password = D6JNKKULHN3G5B8A
acct2.CertKey=password
acct2.CertPath=resource/sdk-cert.p12
acct2.AppId=APP-80W284485P519543T
```

##### Third party API calls

You can execute third party API calls on behalf of other paypal accounts. You can use the [Permissions API](https://developer.paypal.com/webapps/developer/docs/classic/permissions-service/integration-guide/PermissionsAbout/) to get an authorization token and secret for a third party.

**Sample token based third party configuration**
```java
ThirdPartyAuthorization thirdPartyAuth = new TokenAuthorization("accessToken","tokenSecret");
SignatureCredential cred = new SignatureCredential("jb-us-seller_api1.paypal.com","WX4WTU3S8MY44S7F",
"AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
cred.setApplicationId("APP-80W284485P519543T");
cred.setThirdPartyAuthorization(thirdPartyAuth);
MassPayResponse response = service.massPay(massPayReq, cred);
```

#### Connection Information

* defaults to 5000 if not specified
  ```properties
  http.ConnectionTimeOut=5000
  ```
* defaults to 2 if not specified
  ```properties
  http.Retry=2
  ```
* defaults to 30000 if not specified
  ```properties
  http.ReadTimeOut=30000
  ```
* defaults to 100 if not specified
  ```properties
  http.MaxConnection=100
  ```
* defaults to 127.0.0.1 if not specified
  ```properties
  http.IPAddress=127.0.0.1
  ```

#### HTTP Proxy configuration

* If you are using proxy set http.UseProxy to true and replace the following values with your proxy parameters
  ```properties
  http.UseProxy=false
  http.ProxyPort=8080
  http.ProxyHost=127.0.0.1
  http.ProxyUserName=null
  http.ProxyPassword=null
  ```

#### Google App Engine configuration

* Set this property to true if you are using the PayPal SDK within a Google App Engine java app
* defaults to false if not specified
  ```properties
  http.GoogleAppEngine=false
  ```
#### Developer Sandbox Email Address

* Developer email address is required for account creation in sandbox environment
  ```properties
  sandbox.EmailAddress=pp.devtools@gmail.com
  ```

#### Examples

 * sdk_config.properties
```properties
# Service Configuration
mode=sandbox

# Account credentials (Add one or more API accounts here)

acct1.UserName = jb-us-seller_api1.paypal.com
acct1.Password = WX4WTU3S8MY44S7F
acct1.Signature = AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy
acct1.AppId=APP-80W284485P519543T

acct2.UserName = certuser_biz_api1.paypal.com
acct2.Password = D6JNKKULHN3G5B8A
acct2.CertKey=password
# replace the below path with absolute certificate path.
acct2.CertPath=src/test/resources/sdk-cert.p12
acct2.AppId=APP-80W284485P519543T

# Connection Information
http.ConnectionTimeOut=5000
http.Retry=2
http.ReadTimeOut=30000
http.MaxConnection=100
http.IPAddress=127.0.0.1


# HTTP Proxy configuration
# If you are using proxy set http.UseProxy to true and replace the following values with your proxy parameters
http.ProxyPort=8080
http.ProxyHost=127.0.0.1
http.UseProxy=false
http.ProxyUserName=null
http.ProxyPassword=null

#Set this property to true if you are using the PayPal SDK within a Google App Engine java app
http.GoogleAppEngine = false
```

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
