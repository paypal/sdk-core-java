package com.paypal.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import com.paypal.exception.SSLConfigurationException;

public abstract class SSLUtil {

	/**
	 * KeyManagerFactory used for {@link SSLContext} {@link KeyManager}
	 */
	private static final KeyManagerFactory KMF;

	/**
	 * Private {@link Map} used for caching {@link KeyStore}s
	 */
	private static final Map<String, KeyStore> STOREMAP;

	static {
		try {
			
			// Initialize KeyManagerFactory and local KeyStore cache
			KMF = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			STOREMAP = new HashMap<String, KeyStore>();
		} catch (NoSuchAlgorithmException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Returns a SSLContext
	 * 
	 * @param keymanagers
	 *            KeyManager[] The key managers
	 * @return SSLContext with proper client certificate
	 * @throws SSLConfigurationException
	 * @throws IOException
	 *             if an IOException occurs
	 */
	public static SSLContext getSSLContext(KeyManager[] keymanagers)
			throws SSLConfigurationException {
		try {
			SSLContext ctx = SSLContext.getInstance("SSL"); // TLS, SSLv3, SSL
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(System.currentTimeMillis());
			ctx.init(keymanagers, null, random);
			return ctx;
		} catch (Exception e) {
			throw new SSLConfigurationException(e.getMessage(), e);
		}
	}

	/**
	 * Retrieves keyStore from the cached {@link Map}, if not present loads
	 * certificate into java keyStore and caches it for further references
	 * 
	 * @param p12Path
	 *            Path to the client certificate
	 * @param password
	 *            {@link KeyStore} password
	 * @return keyStore {@link KeyStore} loaded with the certificate
	 * @throws NoSuchProviderException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static KeyStore p12ToKeyStore(String p12Path, String password)
			throws NoSuchProviderException, KeyStoreException,
			CertificateException, NoSuchAlgorithmException, IOException {
		KeyStore keyStore = STOREMAP.get(p12Path);
		if (keyStore == null) {
			keyStore = KeyStore.getInstance("PKCS12", "SunJSSE");
			FileInputStream in = null;
			try {
				in = new FileInputStream(p12Path);
				keyStore.load(in, password.toCharArray());
				STOREMAP.put(p12Path, keyStore);
			} finally {
				if (in != null) {
					in.close();
				}
			}
		}
		return keyStore;
	}

	/**
	 * Create a SSLContext with provided client certificate
	 * 
	 * @param certPath
	 * @param certPassword
	 * @return SSLContext
	 * @throws SSLConfigurationException
	 */
	public static SSLContext setupClientSSL(String certPath, String certPassword)
			throws SSLConfigurationException {
		SSLContext sslContext = null;
		try {
			KeyStore ks = p12ToKeyStore(certPath, certPassword);
			KMF.init(ks, certPassword.toCharArray());
			sslContext = getSSLContext(KMF.getKeyManagers());
		} catch (NoSuchAlgorithmException e) {
			throw new SSLConfigurationException(e.getMessage(), e);
		} catch (KeyStoreException e) {
			throw new SSLConfigurationException(e.getMessage(), e);
		} catch (UnrecoverableKeyException e) {
			throw new SSLConfigurationException(e.getMessage(), e);
		} catch (CertificateException e) {
			throw new SSLConfigurationException(e.getMessage(), e);
		} catch (NoSuchProviderException e) {
			throw new SSLConfigurationException(e.getMessage(), e);
		} catch (IOException e) {
			throw new SSLConfigurationException(e.getMessage(), e);
		}
		return sslContext;
	}
}
