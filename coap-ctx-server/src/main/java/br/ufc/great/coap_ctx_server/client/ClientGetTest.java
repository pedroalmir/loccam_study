package br.ufc.great.coap_ctx_server.client;

import org.eclipse.californium.core.CoapClient;

public class ClientGetTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// testing for actuators
		CoapClient actuatorsFilterClient = AFClientRequest.actuatorsRequest();
		System.out.println(actuatorsFilterClient.get().getResponseText());
        //System.out.println("Discovered: "+actuatorsFilterClient.discover().toString());
	}
}