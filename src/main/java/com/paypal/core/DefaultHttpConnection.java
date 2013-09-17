package com.paypal.core;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import com.paypal.exception.SSLConfigurationException;

/**
 * Wrapper class used for HttpsURLConnection
 * 
 */
public class DefaultHttpConnection extends HttpConnection {

	/**
	 * Secure Socket Layer context
	 */
	private SSLContext sslContext;

	public DefaultHttpConnection() {
		try {
			sslContext = SSLUtil.getSSLContext(null);
		} catch (SSLConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setupClientSSL(String certPath, String certKey)
			throws SSLConfigurationException {
		try {
			this.sslContext = SSLUtil.setupClientSSL(certPath, certKey);
		} catch (Exception e) {
			throw new SSLConfigurationException(e.getMessage(), e);
		}
	}

	@Override
	public void createAndconfigureHttpConnection(
			HttpConfiguration clientConfiguration) throws IOException {
		this.config = clientConfiguration;
		URL url = new URL(this.config.getEndPointUrl());
		Proxy proxy = null;
		String proxyHost = this.config.getProxyHost();
		int proxyPort = this.config.getProxyPort();
		if ((proxyHost != null) && (proxyPort > 0)) {
			SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
			proxy = new Proxy(Proxy.Type.HTTP, addr);
		}
		if (proxy != null) {
			this.connection = (HttpURLConnection) url.openConnection(proxy);
		} else {
			this.connection = (HttpURLConnection) url
					.openConnection(Proxy.NO_PROXY);
		}
		if (this.connection instanceof HttpsURLConnection) {
			((HttpsURLConnection) this.connection)
					.setSSLSocketFactory(this.sslContext.getSocketFactory());
		}

		if (this.config.getProxyUserName() != null
				&& this.config.getProxyPassword() != null) {
			final String username = this.config.getProxyUserName();
			final String password = this.config.getProxyPassword();
			Authenticator authenticator = new DefaultPasswordAuthenticator(
					username, password);
			Authenticator.setDefault(authenticator);
		}

		System.setProperty("http.maxConnections",
				String.valueOf(this.config.getMaxHttpConnection()));
		System.setProperty("sun.net.http.errorstream.enableBuffering", "true");
		this.connection.setDoInput(true);
		this.connection.setDoOutput(true);
		this.connection.setRequestMethod(config.getHttpMethod());
		this.connection.setConnectTimeout(this.config.getConnectionTimeout());
		this.connection.setReadTimeout(this.config.getReadTimeout());
	}

	/**
	 * Private class for password based authentication
	 * 
	 * @author kjayakumar
	 * 
	 */
	private static class DefaultPasswordAuthenticator extends Authenticator {

		/**
		 * Username
		 */
		private String userName;

		/**
		 * Password
		 */
		private String password;

		public DefaultPasswordAuthenticator(String userName, String password) {
			this.userName = userName;
			this.password = password;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return (new PasswordAuthentication(userName, password.toCharArray()));
		}
	}

}