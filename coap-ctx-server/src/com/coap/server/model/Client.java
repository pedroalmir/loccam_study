/**
 * 
 */
package com.coap.server.model;

import java.util.LinkedHashMap;

/**
 * @author RubensSilva
 */
public class Client {
	
	/** Unique identification. E.g. stemp01 */
	private String uid;
	
	/** roles: actuator or sensor */
	private String role;
	
	/** Resource role: e.g.: temperature, pressure, lightness, print */
	private String location;
	
	/** Client IP */
	private String ip;
	
	/** Client context. E.g.: <"temperature", "29ºC"> */
	private LinkedHashMap<String, String> context;
	
	/**
	 * @param uid
	 * @param role
	 * @param location
	 * @param contextRole
	 * @param ip
	 */
	public Client(String uid, String role, String location, String ip) {
		this.uid = uid;
		this.role = role;
		this.location = location;
		
		this.ip = ip;
		this.context = new LinkedHashMap<>();
	}

	/**
	 * @param variable
	 * @param value
	 */
	public void addContext(String variable, String value){
		this.context.put(variable, value);
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the location
	 */
	public String getlocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setlocation(String location) {
		this.location = location;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the context
	 */
	public LinkedHashMap<String, String> getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(LinkedHashMap<String, String> context) {
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Client [uid=" + uid + ", role=" + role + ", location=" + location + ", ip=" + ip + ", context=" + context + "]";
	}
}
