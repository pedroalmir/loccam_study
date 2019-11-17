package com.coap.server.client;

import org.eclipse.californium.core.CoapClient;

public class GetSensorsTest {
	public static void main(String[] args) {
		
		// testing for actuators
		CoapClient sensorsFilterClient = AFClientRequest.sensorsRequest();
		
		
		System.out.println(sensorsFilterClient.get().getResponseText());
        
	}
}