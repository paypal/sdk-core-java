CHANGELOG
=========

v1.7.2
------

   * Utility classes should not have public constructors [#64] (https://github.com/paypal/sdk-core-java/pull/64)
   * Dead stores should be removed [#63] (https://github.com/paypal/sdk-core-java/pull/63)

v1.7.1
------

   * Fixed config file load from path with whitespace [#62] (https://github.com/paypal/sdk-core-java/pull/62)
   * Added expires check for token call [#61] (https://github.com/paypal/sdk-core-java/pull/61)
   * Fixed time mismatch issue for OAuthToken expire [#60] (https://github.com/paypal/sdk-core-java/pull/60)
   
v1.7.0
------

   * TLS 1.2 support

v1.6.9 (June 9, 2015)
---------------------

   * Fix for IPNListner URL encoding

v1.6.8 (April 23, 2015)
---------------------

   * Fix for IPNListener behind firewall
   * Made sdk_config.properties optional
   
v1.6.7 (March 31, 2015)
---------------------

   * Made SSL connection configurable

v1.6.6 (December 11, 2014)
---------------------

   * Added content-type header when creating OAuth token

v1.6.5 (November 10, 2014)
---------------------

   * Added user-agent header to classic SDK codebase

v1.6.4 (October 16, 2014)
---------------------

   * Change SSL to TLS in SSLUtil.java for SSL Poodle issue

v1.6.3 (October 13, 2014)
---------------------

   * Make PayPalResource.initConfig() return OAuthTokenCredential

v1.6.2 (August 13, 2014)
---------------------

   * Change scope of QueryParameters's method
   * Fix for issue [#28] (https://github.com/paypal/sdk-core-java/issues/28)

v1.6.1 (June 16, 2014)
---------------------

   * Fix connection reattempt logic

v1.6.0 (May 13, 2014)
---------------------

   * Update core to support future payment

v1.5.0 (September 26, 2013)
---------------------

   * Updating core to support genio
