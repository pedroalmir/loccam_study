package br.ufc.great.coap_ctx_server.skeleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.californium.core.server.resources.Resource;

public class Skeleton {
	
	/**
	 * Device [uid=sgps01, type=sensor, resourceType=location, contextType=50, ip=fe80:0:0:0:b4f6:7d2e:145e:1ce9%eth7, context={}]
	 *
	 * on filter: soprinter01
	 * > path: /devices/
	 * > uri: /devices/soprinter01
	 * > class: class org.eclipse.californium.core.CoapResource
	 * > attributes>resourceTypes: [printer]
	 * > attributes>contentTypes: [50]
	 * > attributes>interfaceDescriptions: [smart object]
	 * > attributes>title: null
	 * > children: []
	 * 
	 * Response:
	 * 
	 * Discovered: [</.well-known/core> null, </devices> devices, </devices/adoor01> null
	 * rt:	[door]
	 * if:	[actuator]
	 * ct:	[50], </devices/sgps01> null
	 * rt:	[location]
	 * if:	[sensor]
	 * ct:	[50], </devices/slight01> null
	 * rt:	[lightness]
	 * if:	[sensor]
	 * ct:	[50], </devices/soprinter01> null
	 * rt:	[printer]
	 * if:	[smart, object]
	 * ct:	[50], </devices/stemp01> null
	 * rt:	[temperature]
	 * if:	[sensor]
	 * ct:	[50]]
	 **/
    public Collection<Resource> tempSkeleton(int temp, Collection<Resource> children){
    	Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		if(r.getAttributes().getResourceTypes().get(0).equalsIgnoreCase("temperature")){
    			res.add(r);
    		}
    		
    	}
    	
    	return res;
    }
    
    public Collection<Resource> tempInsideIntervalSkeleton(int temp, List<Integer> interval, Collection<Resource> children){
    	Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		if(r.getAttributes().getResourceTypes().get(0).equalsIgnoreCase("temperature")){
    			int tempValue = Integer.parseInt(r.getAttributes().getAttributeValues("value").get(0));
    			if(tempValue >= interval.get(0) && tempValue <= interval.get(1)) {
    				res.add(r);
    			}
    		}
    		
    	}
    	
    	return res;
    }
    
    public Collection<Resource> umiditySkeleton(int umid, Collection<Resource> children){
    	// getAttributes > getResourseType is where i must look at
    	Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		if(r.getAttributes().getResourceTypes().get(0).equalsIgnoreCase("umidity")) {
    			res.add(r);
    		}
    	}
    	return res;
    }
    
    public Collection<Resource> umidityInsideIntervalSkeleton(int umid, List<Integer> interval, Collection<Resource> children){
    	// getAttributes > getResourseType is where i must look at
    	Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		if(r.getAttributes().getResourceTypes().get(0).equalsIgnoreCase("umidity")) {
    			int umidValue = Integer.parseInt(r.getAttributes().getAttributeValues("value").get(0));
    			if(umidValue >= interval.get(0) && umidValue <= interval.get(1)) {
    				res.add(r);
    			}
    		}
    	}
    	return res;
    }
    
    public Collection<Resource> dateSkeleton(String date, String hour, Collection<Resource> children){
    	return children;
    }
    
    public Collection<Resource> networkSkeleton(String network, Collection<Resource> children){
    	Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		String ip = r.getAttributes().getAttributeValues("ip").get(0);
    		if(network.equalsIgnoreCase(ip)) {
    			res.add(r);
    		}
    	}
    	return res;
    }
    
    /*
     * Cant do it right now
     * */
    public  Collection<Resource> activitySkeleton(String activity, Collection<Resource> children){
    	return children;
    }
    
    /*
     * Cant do it right now
     * */
    public Collection<Resource> envSkeleton(String env, Collection<Resource> children){
    	Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		String location = r.getAttributes().getAttributeValues("location").get(0);
    		if(env.equalsIgnoreCase(location)) {
    			res.add(r);
    		}
    	}
    	return res;
    }
    
    public Collection<Resource> sensorSkeleton(Collection<Resource> children){
    	// getAttributes > getInterfaceDescriptions is where i must look
    	Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		if(r.getAttributes().getInterfaceDescriptions().get(0).equalsIgnoreCase("sensor")) {
    			res.add(r);
    		}
    	}
    	return res;
    }
    
    public Collection<Resource> actuatorSkeleton(Collection<Resource> children){
    	// getAttributes > getInterfaceDescriptions is where i must look
    	Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		if(r.getAttributes().getInterfaceDescriptions().get(0).equalsIgnoreCase("actuator")) {
    			res.add(r);
    		}
    	}
    	return res;
    }
    
    public Collection<Resource> proximitySkeleton(Double meters, Double lat, Double lng, Collection<Resource> children){
    	Collection<Resource> res = new ArrayList<Resource>();
    	
    	// here, all the devices must have lat and lng 
    	for(Resource r : children) {
    		Double devLat = Double.parseDouble(r.getAttributes().getAttributeValues("lat").get(0));
    		Double devLng= Double.parseDouble(r.getAttributes().getAttributeValues("lng").get(0));
    		
    		Double dist = Math.sqrt(Math.pow(devLat - lat, 2) + Math.pow(devLng - lng, 2));
    		
    		if(dist <= meters) {
    			res.add(r);
    		}
    	}
    	return res;
    }
}
