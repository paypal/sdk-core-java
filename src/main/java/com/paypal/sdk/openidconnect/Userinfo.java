package com.paypal.sdk.openidconnect;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.HttpMethod;
import com.paypal.core.rest.JSONFormatter;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;
import com.paypal.core.rest.RESTUtil;

public class Userinfo {

	/**
	 * Subject - Identifier for the End-User at the Issuer.
	 */
	private String userId;
	
	/**
	 * Subject - Identifier for the End-User at the Issuer.
	 */
	private String sub;
	
	/**
	 * End-User's full name in displayable form including all name parts, possibly including titles and suffixes, ordered according to the End-User's locale and preferences.
	 */
	private String name;
	
	/**
	 * Given name(s) or first name(s) of the End-User
	 */
	private String givenName;
	
	/**
	 * Surname(s) or last name(s) of the End-User.
	 */
	private String familyName;
	
	/**
	 * Middle name(s) of the End-User.
	 */
	private String middleName;
	
	/**
	 * URL of the End-User's profile picture.
	 */
	private String picture;
	
	/**
	 * End-User's preferred e-mail address.
	 */
	private String email;
	
	/**
	 * True if the End-User's e-mail address has been verified; otherwise false.
	 */
	private Boolean emailVerified;
	
	/**
	 * End-User's gender.
	 */
	private String gender;
	
	/**
	 * End-User's birthday, represented as an YYYY-MM-DD format. They year MAY be 0000, indicating it is omited. To represent only the year, YYYY format would be used.
	 */
	private String birthdate;
	
	/**
	 * Time zone database representing the End-User's time zone
	 */
	private String zoneinfo;
	
	/**
	 * End-User's locale.
	 */
	private String locale;
	
	/**
	 * End-User's preferred telephone number.
	 */
	private String phoneNumber;
	
	/**
	 * End-User's preferred address.
	 */
	private Address address;
	
	/**
	 * Verified account status.
	 */
	private Boolean verifiedAccount;
	
	/**
	 * Account type.
	 */
	private String accountType;
	
	/**
	 * Account holder age range.
	 */
	private String ageRange;
	
	/**
	 * Account payer identifier.
	 */
	private String payerId;
	
	/**
	 * Returns the last request sent to the Service
	 * 
	 * @return Last request sent to the server
	 */
	public static String getLastRequest() {
		return PayPalResource.getLastRequest();
	}

	/**
	 * Returns the last response returned by the Service
	 * 
	 * @return Last response got from the Service
	 */
	public static String getLastResponse() {
		return PayPalResource.getLastResponse();
	}

	/**
	 * Initialize using InputStream(of a Properties file)
	 * 
	 * @param is
	 *            InputStream
	 * @throws PayPalRESTException
	 */
	public static void initConfig(InputStream is) throws PayPalRESTException {
		PayPalResource.initConfig(is);
	}

	/**
	 * Initialize using a File(Properties file)
	 * 
	 * @param file
	 *            File object of a properties entity
	 * @throws PayPalRESTException
	 */
	public static void initConfig(File file) throws PayPalRESTException {
		PayPalResource.initConfig(file);
	}

	/**
	 * Initialize using Properties
	 * 
	 * @param properties
	 *            Properties object
	 */
	public static void initConfig(Properties properties) {
		PayPalResource.initConfig(properties);
	}
	/**
	 * Default Constructor
	 */
	public Userinfo() {
	}

	/**
	 * Setter for userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
 	}
 	
 	/**
	 * Getter for userId
	 */
	public String getUserId() {
		return this.userId;
	}
	
	/**
	 * Setter for sub
	 */
	public void setSub(String sub) {
		this.sub = sub;
 	}
 	
 	/**
	 * Getter for sub
	 */
	public String getSub() {
		return this.sub;
	}
	
	/**
	 * Setter for name
	 */
	public void setName(String name) {
		this.name = name;
 	}
 	
 	/**
	 * Getter for name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Setter for givenName
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
 	}
 	
 	/**
	 * Getter for givenName
	 */
	public String getGivenName() {
		return this.givenName;
	}
	
	/**
	 * Setter for familyName
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
 	}
 	
 	/**
	 * Getter for familyName
	 */
	public String getFamilyName() {
		return this.familyName;
	}
	
	/**
	 * Setter for middleName
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
 	}
 	
 	/**
	 * Getter for middleName
	 */
	public String getMiddleName() {
		return this.middleName;
	}
	
	/**
	 * Setter for picture
	 */
	public void setPicture(String picture) {
		this.picture = picture;
 	}
 	
 	/**
	 * Getter for picture
	 */
	public String getPicture() {
		return this.picture;
	}
	
	/**
	 * Setter for email
	 */
	public void setEmail(String email) {
		this.email = email;
 	}
 	
 	/**
	 * Getter for email
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * Setter for emailVerified
	 */
	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
 	}
 	
 	/**
	 * Getter for emailVerified
	 */
	public Boolean getEmailVerified() {
		return this.emailVerified;
	}
	
	/**
	 * Setter for gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
 	}
 	
 	/**
	 * Getter for gender
	 */
	public String getGender() {
		return this.gender;
	}
	
	/**
	 * Setter for birthdate
	 */
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
 	}
 	
 	/**
	 * Getter for birthdate
	 */
	public String getBirthdate() {
		return this.birthdate;
	}
	
	/**
	 * Setter for zoneinfo
	 */
	public void setZoneinfo(String zoneinfo) {
		this.zoneinfo = zoneinfo;
 	}
 	
 	/**
	 * Getter for zoneinfo
	 */
	public String getZoneinfo() {
		return this.zoneinfo;
	}
	
	/**
	 * Setter for locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
 	}
 	
 	/**
	 * Getter for locale
	 */
	public String getLocale() {
		return this.locale;
	}
	
	/**
	 * Setter for phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
 	}
 	
 	/**
	 * Getter for phoneNumber
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	/**
	 * Setter for address
	 */
	public void setAddress(Address address) {
		this.address = address;
 	}
 	
 	/**
	 * Getter for address
	 */
	public Address getAddress() {
		return this.address;
	}
	
	/**
	 * Setter for verifiedAccount
	 */
	public void setVerifiedAccount(Boolean verifiedAccount) {
		this.verifiedAccount = verifiedAccount;
 	}
 	
 	/**
	 * Getter for verifiedAccount
	 */
	public Boolean getVerifiedAccount() {
		return this.verifiedAccount;
	}
	
	/**
	 * Setter for accountType
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
 	}
 	
 	/**
	 * Getter for accountType
	 */
	public String getAccountType() {
		return this.accountType;
	}
	
	/**
	 * Setter for ageRange
	 */
	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
 	}
 	
 	/**
	 * Getter for ageRange
	 */
	public String getAgeRange() {
		return this.ageRange;
	}
	
	/**
	 * Setter for payerId
	 */
	public void setPayerId(String payerId) {
		this.payerId = payerId;
 	}
 	
 	/**
	 * Getter for payerId
	 */
	public String getPayerId() {
		return this.payerId;
	}
	
	
	/**
	 * Returns user details
	 * 
	 * @param userinfoParameters
	 *            Query parameters used for API call
	 * @return Userinfo
	 * @throws PayPalRESTException
	 */
	public static Userinfo getUserinfo(UserinfoParameters userinfoParameters)
			throws PayPalRESTException {
		return getUserinfo(null, userinfoParameters);
	}

	/**
	 * Returns user details
	 * 
	 * @param apiContext
	 *            {@link APIContext} to be used for the call.
	 * @param userinfoParameters
	 *            Query parameters used for API call
	 * @return Userinfo
	 * @throws PayPalRESTException
	 */
	public static Userinfo getUserinfo(APIContext apiContext,
			UserinfoParameters userinfoParameters) throws PayPalRESTException {
		String pattern = "v1/identity/openidconnect/userinfo?schema={0}&access_token={1}";
		Object[] parameters = new Object[] { userinfoParameters };
		String resourcePath = RESTUtil.formatURIPath(pattern, parameters);
		String payLoad = "";
		return PayPalResource.configureAndExecute(apiContext, HttpMethod.GET,
				resourcePath, payLoad, Userinfo.class);
	}

	/**
	 * Returns a JSON string corresponding to object state
	 * 
	 * @return JSON representation
	 */
	public String toJSON() {
		return JSONFormatter.toJSON(this);
	}

	@Override
	public String toString() {
		return toJSON();
	}
}