package com.coap.server.client;

import java.util.HashMap;

import org.eclipse.californium.core.CoapClient;


public class GetTest {
	public static void main(String[] args) {
		
		// testing for actuators
		HashMap<String, String> parames = new HashMap<String, String>();
		// getting the sensors
		parames.put("if", "actuator");
		parames.put("rt", "temperature");
		parames.put("temperature", "22");
		
		CoapClient actuatorsFilterClient = AFClientRequest.customLocalRequest(parames);
		
		
		System.out.println(actuatorsFilterClient.get().getResponseText());
        
	}
}