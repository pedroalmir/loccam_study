package com.coap.server.client;

import java.util.Collection;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import com.google.gson.Gson;

public class ClientCoapHandler implements CoapHandler {
	
	private String uid;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public ClientCoapHandler(String uid) {
		this.uid = uid;
	}

	/**
	 * Invoked when a CoAP response or notification has arrived.
	 *
	 * @param response the response
	 */
	public void onLoad(CoapResponse response) {
		if(response != null){
        	//System.out.println("- RESPONSE: "+ new Gson().fromJson(response.getResponseText(), Collection.class) );
			System.out.println("* Response: " + response.getResponseText());
			System.out.println(">> payload"+ response.getPayload());
			System.out.println(">> options"+ response.getOptions());
			System.out.println(">> code"+ response.getCode());
        }else{
        	System.out.println("Request failed");
        }
	}
	
	/**
	 * Invoked when a request timeouts or has been rejected by the server.
	 */
	public void onError() {
		System.out.println("!!! Error in client handler !!!");
	}

}