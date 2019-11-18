package com.coap.server.skeleton;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.californium.core.server.resources.Resource;

public class Skeleton {
	
	/*
	 * Device [uid=sgps01, type=sensor, resourceType=location, contextType=50, ip=fe80:0:0:0:b4f6:7d2e:145e:1ce9%eth7, context={}]

	 * on filter: soprinter01
	> path: /devices/
	> uri: /devices/soprinter01
	> class: class org.eclipse.californium.core.CoapResource
	> attributes>resourceTypes: [printer]
	> attributes>contentTypes: [50]
	> attributes>interfaceDescriptions: [smart object]
	> attributes>title: null
	> children: []
	
	Response:
	
	Discovered: [</.well-known/core> null, </devices> devices, </devices/adoor01> null
	rt:	[door]
	if:	[actuator]
	ct:	[50], </devices/sgps01> null
	rt:	[location]
	if:	[sensor]
	ct:	[50], </devices/slight01> null
	rt:	[lightness]
	if:	[sensor]
	ct:	[50], </devices/soprinter01> null
	rt:	[printer]
	if:	[smart, object]
	ct:	[50], </devices/stemp01> null
	rt:	[temperature]
	if:	[sensor]
	ct:	[50]]
	 * */

	public Collection<Resource> resourceType(String type, Collection<Resource> children){
		Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		if(r.getAttributes().getResourceTypes().get(0).equalsIgnoreCase(type)){
    			res.add(r);
    		}
    		
    	}
    	
    	return res;
	}
	
	public Collection<Resource> locationSkt(String location, Collection<Resource> children){
		Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		for(String loc : r.getAttributes().getAttributeKeySet()){
    			if(r.getAttributes().getAttributeValues(loc).get(0).equalsIgnoreCase(location)) {
    				res.add(r);
    			}
    		}
    	}
    	
    	return res;
	}
	
	public Collection<Resource> contextSkt(String att, Collection<Resource> children){
		Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		for(String loc : r.getAttributes().getAttributeKeySet()){
    			if(r.getAttributes().getAttributeValues(loc).get(0).equalsIgnoreCase(att)) {
    				res.add(r);
    			}
    		}
    	}
    	
    	return res;
	}
	
	public Collection<Resource> contextSkt(String type, String att, Collection<Resource> children){
		Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		for(String ctx : r.getAttributes().getAttributeKeySet()){
    			// tests if type is equal the key eg temperature &&
    			// tests if temperature == att
    			if(
					ctx.equalsIgnoreCase(type) && 
    				r.getAttributes().getAttributeValues(ctx).get(0).equalsIgnoreCase(att)
				) {
    				res.add(r);
    			}
    		}
    	}
    	
    	return res;
	}
	
	public  Collection<Resource> typeSkt(String type, Collection<Resource> children){
    	
    	// getAttributes > getInterfaceDescriptions is where i must look
    	Collection<Resource> res = new ArrayList<Resource>();
    	for(Resource r : children) {
    		
    		if(r.getAttributes().getInterfaceDescriptions().get(0).equalsIgnoreCase(type)) {
    			res.add(r);
    		}
    		
    	}
    	
    	return res;
    	
    }
}
