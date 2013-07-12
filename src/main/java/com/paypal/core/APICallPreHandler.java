package com.paypal.core;

import java.util.Map;

import com.paypal.core.credential.ICredential;
import com.paypal.core.nvp.PlatformAPICallPreHandler;
import com.paypal.core.rest.RESTAPICallPreHandler;
import com.paypal.core.soap.MerchantAPICallPreHandler;
import com.paypal.exception.ClientActionRequiredException;
import com.paypal.sdk.exceptions.OAuthException;

/**
 * <code>APICallPreHandler</code> defines a high level abstraction for call
 * specific operations. The calls may be divided as per formats as SOAP or NVP.
 * PayPal Merchant Services are provided by {@link MerchantAPICallPreHandler} which
 * is a decorator over the basic {@link DefaultSOAPAPICallHandler}. PayPal Platform
 * Services are provided by {@link PlatformAPICallPreHandler}. PayPal REST API is
 * provided by {@link RESTAPICallPreHandler}
 * 
 */
public interface APICallPreHandler {

	/**
	 * Returns headers for HTTP call
	 * 
	 * @return Map of headers with name and value
	 * @throws OAuthException
	 */
	Map<String, String> getHeaderMap() throws OAuthException;

	/**
	 * Returns the payload for the API call. The implementation should take care
	 * in formatting the payload appropriately
	 * 
	 * @return Payload as String
	 */
	String getPayLoad();

	/**
	 * Returns the endpoint for the API call. The implementation may calculate
	 * the endpoint depending on parameters set on it. If no endpoint is found
	 * in the passed configuration, then SANDBOX endpoints (hardcoded in
	 * {@link Constants})are taken to be default for the API call.
	 * 
	 * @return Endpoint String.
	 */
	String getEndPoint();

	/**
	 * Returns {@link ICredential} configured for the api call
	 * 
	 * @return ICredential object
	 */
	ICredential getCredential();

	/**
	 * Validates settings and integrity before call
	 * 
	 * @throws ClientActionRequiredException
	 */
	void validate() throws ClientActionRequiredException;

}
