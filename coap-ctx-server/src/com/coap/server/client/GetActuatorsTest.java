package com.coap.server.client;

import org.eclipse.californium.core.CoapClient;

public class GetActuatorsTest {
	public static void main(String[] args) {
		
		// testing for actuators
		CoapClient actuatorsFilterClient = AFClientRequest.actuatorsRequest();
		
		
		System.out.println(actuatorsFilterClient.get().getResponseText());
        
	}
}