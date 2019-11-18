package com.coap.server.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.coap.server.model.Device;
import com.coap.server.types.ResourceTypes;
import com.coap.server.types.Types;

public abstract class Utils {
	public static String getMyIP(){
		String ip = null;
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp())
	                continue;

	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) {
	                InetAddress addr = addresses.nextElement();
	                ip = addr.getHostAddress();
	                System.out.println("***"+iface.getDisplayName() + " " + ip);
	            }
	        }
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }
	    return ip;
	}
	
	/*
	 * Creates the default devices on smart ambient
	 * */
	public static ArrayList<Device> createPresetDevices() {
		ArrayList<Device> devices = new ArrayList<Device>();
		
		Device airClass = new Device(
				"dev0" + "_" + Types.ACTUATOR.getValor(), 
				Types.ACTUATOR.getValor(), 
				ResourceTypes.TEMPERATURE.getValor(), 
				MediaTypeRegistry.APPLICATION_JSON, 
				getMyIP(), 
				"classroom"
			);
		airClass.addContext("temperature", "20");
		devices.add(airClass);
		
		Device tempSensorClass = new Device(
				"dev1" + "_" + Types.SENSOR.getValor(), 
				Types.SENSOR.getValor(), 
				ResourceTypes.TEMPERATURE.getValor(), 
				MediaTypeRegistry.APPLICATION_JSON, 
				getMyIP(),
				"classroom"
			);
		tempSensorClass.addContext("temperature", "18");
		devices.add(tempSensorClass);
		
		Device airHall= new Device(
				"dev2" + "_" + Types.ACTUATOR.getValor(), 
				Types.ACTUATOR.getValor(), 
				ResourceTypes.TEMPERATURE.getValor(), 
				MediaTypeRegistry.APPLICATION_JSON, 
				getMyIP(),
				"hall"
			);
		airHall.addContext("temperature", "22");
		devices.add(airHall);
		
		Device tempSensorHall = new Device(
				"dev3" + "_" + Types.SENSOR.getValor(), 
				Types.SENSOR.getValor(), 
				ResourceTypes.TEMPERATURE.getValor(), 
				MediaTypeRegistry.APPLICATION_JSON, 
				getMyIP(),
				"hall"
			);
		tempSensorHall.addContext("temperature", "23");
		devices.add(tempSensorHall);
		
		
		Device lightClassroom= new Device(
				"dev4" + "_" + Types.ACTUATOR.getValor(), 
				Types.ACTUATOR.getValor(), 
				ResourceTypes.LIGHTNESS.getValor(), 
				MediaTypeRegistry.APPLICATION_JSON, 
				getMyIP(),
				"classroom"
			);
		lightClassroom.addContext("state", "off");
		devices.add(lightClassroom);
		
		Device lightSensorClassroom = new Device(
				"dev5" + "_" + Types.SENSOR.getValor(), 
				Types.SENSOR.getValor(), 
				ResourceTypes.LIGHTNESS.getValor(), 
				MediaTypeRegistry.APPLICATION_JSON, 
				getMyIP(),
				"classroom"
			);
		lightSensorClassroom.addContext("lightness", "0");
		devices.add(lightSensorClassroom);
		
		System.out.println("\n========= ON CREATE DEVICES: \n\t"+ devices.toString() +"\n===========");
		
		return devices;	
		
	}
}