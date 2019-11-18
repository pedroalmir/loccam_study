package com.coap.server.client;

import java.util.HashMap;

import org.eclipse.californium.core.CoapClient;

abstract class AFClientRequest {
	
	/*
	 * returns a coap client searching for all actuators
	 * */
	public static CoapClient actuatorsRequest() {
		return new CoapClient("coap://localhost/devices?if=actuator");
	}
	
	/*
	 * returns a coap client searching for all sensors
	 * */
	public static CoapClient sensorsRequest() {
		return new CoapClient("coap://localhost/devices?if=sensor");
	}
	
	public static CoapClient customLocalRequest(HashMap<String, String> parameters) {
		String parames = "";
		for(String key: parameters.keySet()) {
			parames = parames + key + "=" + parameters.get(key) + "&";
		}
		
		/* removing last '&'*/
		if(!parames.equalsIgnoreCase("")) {
			parames = parames.substring(0, parames.length() - 1);
		}
		
		return new CoapClient("coap://localhost/devices?"+parames);
	}
	
	/*
	 * String parameters comes right after ? in URI 
	 * 
	*/
	public static CoapClient customRequest(String parameters) {
		return new CoapClient("coap://18.229.202.214:5683/.well-known/core?"+parameters);
	}
	
	/**/
	public static CoapClient customCloudRequest(String parameters) {
		return new CoapClient("coap://18.229.202.214:5683/devices?"+parameters);
	}
	
	/**/
	public static CoapClient sensorCloudRequest() {
		return new CoapClient("coap://18.229.202.214:5683/devices?if=sensor");
	}
	
	/**/
	public static CoapClient actuatorCloudRequest() {
		return new CoapClient("coap://18.229.202.214:5683/devices?if=actuator");
	}
	

}