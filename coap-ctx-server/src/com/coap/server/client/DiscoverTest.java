package com.coap.server.client;

import org.eclipse.californium.core.CoapClient;

public class DiscoverTest {
	public static void main(String[] args) {
		
		// testing for actuators
		CoapClient actuatorsFilterClient = AFClientRequest.actuatorsRequest();        
        
        System.out.println("Discovered: "+actuatorsFilterClient.discover().toString());
        
	}
}