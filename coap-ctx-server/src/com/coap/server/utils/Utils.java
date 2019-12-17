package com.coap.server.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import com.coap.server.model.Device;

public abstract class Utils {
	
	public static void main(String[] args) {
		System.out.println(Utils.distance(-3.7544359, -3.7544436, -38.5815867, -38.5815956));
	}
	
	/**
	 * Calculate distance between two points in latitude and longitude taking
	 * into account height difference. If you are not interested in height
	 * difference pass 0.0. Uses Haversine method as its base.
	 * 
	 * lat1, long1 Start point lat2, long2 End point
	 * @returns Distance in Meters
	 */
	public static double distance(double lat1, double lat2, double long1, double long2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(long2 - long1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	    return R * c * 1000; // convert to meters
	}
	
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
		
		String ip = getMyIP();
		
		Device device1 = new Device(ip, "actuator", "airconditioning", "labaula01");
		Device device2 = new Device(ip, "sensor", "temperature", "labaula01");
		Device device3 = new Device(ip, "actuator", "light", "labaula01");
		device1.addContext("state", new Random().nextInt(2) == 0 ? "off" : "on");
		device2.addContext("temperature", String.valueOf(new Random().nextInt(10) + 16));
		device3.addContext("state", new Random().nextInt(2) == 0 ? "off" : "on");
		device1.addContext("latitude", "-3.746452"); device1.addContext("longitude", "-38.578157");
		device2.addContext("latitude", "-3.746452"); device2.addContext("longitude", "-38.578157");
		device3.addContext("latitude", "-3.746452"); device3.addContext("longitude", "-38.578157");
		devices.add(device1); devices.add(device2); devices.add(device3);
		
		device1 = new Device(ip, "actuator", "airconditioning", "labaula02");
		device2 = new Device(ip, "sensor", "temperature", "labaula02");
		device3 = new Device(ip, "actuator", "light", "labaula02");
		device1.addContext("state", new Random().nextInt(2) == 0 ? "off" : "on");
		device2.addContext("temperature", String.valueOf(new Random().nextInt(10) + 16));
		device3.addContext("state", new Random().nextInt(2) == 0 ? "off" : "on");
		device1.addContext("latitude", "-3.746481"); device1.addContext("longitude", "-38.578219");
		device2.addContext("latitude", "-3.746481"); device2.addContext("longitude", "-38.578219");
		device3.addContext("latitude", "-3.746481"); device3.addContext("longitude", "-38.578219");
		devices.add(device1); devices.add(device2); devices.add(device3);
		
		device1 = new Device(ip, "actuator", "airconditioning", "lab10");
		device2 = new Device(ip, "sensor", "temperature", "lab10");
		device3 = new Device(ip, "actuator", "light", "lab10");
		device1.addContext("state", new Random().nextInt(2) == 0 ? "off" : "on");
		device2.addContext("temperature", String.valueOf(new Random().nextInt(10) + 16));
		device3.addContext("state", new Random().nextInt(2) == 0 ? "off" : "on");
		devices.add(device1); devices.add(device2); devices.add(device3);
		
		device1 = new Device(ip, "actuator", "airconditioning", "hall");
		device2 = new Device(ip, "sensor", "temperature", "hall");
		device3 = new Device(ip, "actuator", "light", "hall");
		device1.addContext("state", new Random().nextInt(2) == 0 ? "off" : "on");
		device2.addContext("temperature", String.valueOf(new Random().nextInt(10) + 16));
		device3.addContext("state", new Random().nextInt(2) == 0 ? "off" : "on");
		device1.addContext("latitude", "-3.746466"); device1.addContext("longitude", "-38.578082");
		device2.addContext("latitude", "-3.746466"); device2.addContext("longitude", "-38.578082");
		device3.addContext("latitude", "-3.746466"); device3.addContext("longitude", "-38.578082");
		devices.add(device1); devices.add(device2); devices.add(device3);
		
		device1 = new Device(ip, "actuator", "airconditioning", "conferenceRoom");
		device2 = new Device(ip, "sensor", "temperature", "conferenceRoom");
		device3 = new Device(ip, "actuator", "light", "conferenceRoom");
		device1.addContext("state", new Random().nextInt(2) == 0 ? "off" : "on");
		device2.addContext("temperature", String.valueOf(new Random().nextInt(10) + 16));
		device3.addContext("state", new Random().nextInt(2) == 0 ? "off" : "on");
		device1.addContext("latitude", "-3.746579"); device1.addContext("longitude", "-38.578077");
		device2.addContext("latitude", "-3.746579"); device2.addContext("longitude", "-38.578077");
		device3.addContext("latitude", "-3.746579"); device3.addContext("longitude", "-38.578077");
		devices.add(device1); devices.add(device2); devices.add(device3);
		
		/*device1 = new Device(ip, "sensor", "acc", "ap");
		device1.addContext("network", "CLARO_2GDB49CF");
		device1.addContext("latitude","-3.7544359");
		device1.addContext("longitude", "-38.5815867");
		devices.add(device1);*/
		
		System.out.println("\n========= ON CREATE DEVICES: \n\t"+ devices.toString() +"\n===========");
		
		return devices;	
		
	}
}