package com.paypal.core;

/**
 * APICallPreHandlerFactory factory for returning implementations if
 * {@link APICallPreHandler}
 * 
 * @author kjayakumar
 * 
 */
public interface APICallPreHandlerFactory {

	/**
	 * Creates an instance of {@link APICallPreHandler}
	 * 
	 * @return
	 */
	APICallPreHandler createAPICallPreHandler();

}
