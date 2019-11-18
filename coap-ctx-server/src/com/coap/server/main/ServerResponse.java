package com.coap.server.main;

import java.util.ArrayList;

import org.eclipse.californium.core.server.resources.Resource;

public class ServerResponse {
	ArrayList<Resource> devices;
	
	public ServerResponse(ArrayList<Resource> devices) {
		this.devices = devices;
	}

	public ArrayList<Resource> getDevices() {
		return devices;
	}

	public void setDevices(ArrayList<Resource> devices) {
		this.devices = devices;
	}
	
	
}