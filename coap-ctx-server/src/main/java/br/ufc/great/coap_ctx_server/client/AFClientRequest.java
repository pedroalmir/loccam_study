package br.ufc.great.coap_ctx_server.client;

import org.eclipse.californium.core.CoapClient;

abstract class AFClientRequest {
	
	/**
	 * returns a coap client searching for all actuators
	 * */
	public static CoapClient actuatorsRequest() {
		return new CoapClient("coap://localhost/devices?type=sensor");
	}
	
	/**
	 * returns a coap client searching for all sensors
	 * */
	public static CoapClient sensorsRequest() {
		return new CoapClient("coap://localhost/devices?type=actuator");
	}
	
	/**
	 * String parameters comes right after ? in URI 
	**/
	public static CoapClient customRequest(String parameters) {
		return new CoapClient("coap://18.229.202.214:5683/.well-known/core?"+parameters);
	}
}