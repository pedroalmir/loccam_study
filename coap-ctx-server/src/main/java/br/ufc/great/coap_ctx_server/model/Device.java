/**
 * 
 */
package br.ufc.great.coap_ctx_server.model;

import java.util.LinkedHashMap;

/**
 * @author PedroAlmir
 */
public class Device {
	
	/** Unique identification. E.g. stemp01 */
	private String uid;
	
	/** Types: actuator or sensor */
	private String type;
	
	/** Resource type: e.g.: temperature, pressure, lightness, print */
	private String resourceType;
	
	/** Context Type: e.g.: string, integer, json, ... */
	private int contextType;
	
	/** Device IP */
	private String ip;
	
	/** Device context. E.g.: <"temperature", "29�C"> */
	private LinkedHashMap<String, String> context;
	
	// Location: the name of the room
	private String location;
	
	

	/**
	 * The Device constructor
	 * @param uid
	 * @param type
	 * @param resourceType
	 * @param contextType
	 * @param ip
	 * @param location 
	 */
	public Device(String uid, String type, String resourceType, int contextType, String ip, String location) {
		this.uid = uid;
		this.type = type;
		this.resourceType = resourceType;
		this.contextType = contextType;
		this.ip = ip;
		this.location = location;
		
		this.context = new LinkedHashMap<>();
		
		addContext("location", this.location);
		addContext("ip", this.ip);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the resourceType
	 */
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * @param resourceType the resourceType to set
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * @return the contextType
	 */
	public int getContextType() {
		return contextType;
	}

	/**
	 * @param contextType the contextType to set
	 */
	public void setContextType(int contextType) {
		this.contextType = contextType;
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
		return "Device [uid=" + uid + ", type=" + type + ", resourceType=" + resourceType + ", contextType=" + contextType + ", ip=" + ip + ", context=" + context + "]";
	}
}
