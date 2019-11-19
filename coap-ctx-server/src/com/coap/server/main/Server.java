/*******************************************************************************
 * Licenced under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.coap.server.main;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;

import com.coap.server.model.Device;
import com.coap.server.utils.Utils;
import com.google.gson.Gson;

/**
 * The Class CoAPServer.
 * 
 * @author Yasith Lokuge, Pedro Almir, Rubens Silva
 * 
 * Commands to start/stop this application: nohup java -jar [YourJarPath] &
 * Get process id: ps -ef | grep java
 * Kill process: sudo kill -9 <pid>
 * Tail output file: tail -f nohup.out
 *         
 * Context:
 * - usage
 * - temperature
 * - humidity
 * - latitude, longitude
 * - WiFi network
 * - activity
 * - environment
 * 
 * Requests examples:
 * - coap://<server.ip>:5683/.well-known/core
 * - coap://<server.ip>:5683/devices?if=sensor
 * - coap://<server.ip>:5683/devices?proximity=165,-3.7466212,-38.5769008
 * - coap://<server.ip>:5683/devices?proximity=165,-3.7466212,-38.5769008&if=actuator
 * - coap://<server.ip>:5683/devices?proximity=165,-3.7466212,-38.5769008&if=actuator&ct=10
 *         
 * To run the project, start with this file - some devices are added by default. 
 * The local tests are in client package. For testing in cloud, instantiate the appropriated client the Abstract factory 
 * (e.g., new AFClientRequest.sensorCloudClient() for a client request uri's parameter with filter of 'sensors' for cloud) 
 * - to filter by actuators: run GetAtuatorsTest
 * - to filter by sensors: run GetSensorsTest
 * - to get all: run Discover
 * - to post new five devices: run PostTest
 */
public class Server extends CoapServer {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		try {
			/* Create and start server */
			Server server = new Server();
			server.start();
		} catch (SocketException e) {
			System.err.println("Failed to initialize server: " + e.getMessage());
		}
	}

	/**
	 * Instantiates a new CoAP server.
	 *
	 * @throws SocketException the socket exception
	 */
	public Server() throws SocketException {
		/* Provide an instance of the main resource to register clients */
		add(new RegisterClientsResource());
	}
	
	

	/**
	 * The Class RegisterClientsResource.
	 */
	class RegisterClientsResource extends CoapResource {

		/**
		 * Instantiates a new resource.
		 */
		public RegisterClientsResource() {
			// set resource identifier
			super("devices");
			// set display name
			getAttributes().setTitle("devices");
			
			/* creating the default devices */
			for(Device device : Utils.createPresetDevices()) {
				
				CoapResource newResource = new CoapResource(device.getUid(), true);
				newResource.getAttributes().addContentType(device.getContextType());
				newResource.getAttributes().addResourceType(device.getResourceType());
				newResource.getAttributes().addInterfaceDescription(device.getType());
				
				// all the context goes in this
				for (String key : device.getContext().keySet()) {
					newResource.getAttributes().addAttribute(key, device.getContext().get(key));
				}
				
				add(newResource);
			}
		}

		/**
		 * @see
		 * org.eclipse.californium.core.CoapResource#handlePOST(org.eclipse.californium.
		 * core.server.resources.CoapExchange)
		 */
		@Override
		public void handlePOST(CoapExchange exchange) {
			try {
				Device device = new Gson().fromJson(exchange.getRequestText(), Device.class);
				
				/* Checking for repeated devices */
				for(Resource r : getChildren()) {
					if(r.getName().equalsIgnoreCase(device.getUid())) {
						System.out.print("=== Device already exists: " + device.getUid());
						r.getAttributes().setAttribute(LinkFormat.CONTENT_TYPE, String.valueOf(device.getContextType()));
						r.getAttributes().setAttribute(LinkFormat.RESOURCE_TYPE, device.getResourceType());
						r.getAttributes().setAttribute(LinkFormat.INTERFACE_DESCRIPTION, device.getType());
						
						// all the context goes in this
						for (String key : device.getContext().keySet()) {
							r.getAttributes().setAttribute(key, device.getContext().get(key));
						}
						
						r.getAttributes().setAttribute("uid", device.getUid());
						
						System.out.print(" updated!\n");
						exchange.respond(ResponseCode.CHANGED);
						return;
					}
				}

				/* Creating a new Resource */ 
				CoapResource newResource = new CoapResource(device.getUid(), true);
				newResource.getAttributes().addContentType(device.getContextType());
				newResource.getAttributes().addResourceType(device.getResourceType());
				newResource.getAttributes().addInterfaceDescription(device.getType());
				
				// Setting context values...
				for (String key : device.getContext().keySet()) {
					newResource.getAttributes().addAttribute(key, device.getContext().get(key));
				}
				
				newResource.getAttributes().setAttribute("uid", device.getUid());
				
				add(newResource);
				exchange.respond(ResponseCode.CREATED);
				System.out.println("=== CREATED: " + device.toString());
			} catch (Exception ex) {
				exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
			}
		}
		

	    @Override
	    public void handleGET(CoapExchange exchange) {
	    	System.out.println("=======================");
	    	try {
	    		System.out.println("\n*****");
	    		System.out.println("Children: " + getChildren());
		    	
	    		System.out.println("> Exchange \n");
	    		System.out.println("> RequestText: " + exchange.getRequestText());
	    		System.out.println("> Request payload: " + exchange.getRequestPayload());
	    		System.out.println("> Source address (ip): " + exchange.getSourceAddress());
	    		System.out.println("> Source port: " + exchange.getSourcePort());
	    		System.out.println("> Request code: " + exchange.getRequestCode());
	    		System.out.println("> Request options: " + exchange.getRequestOptions());
	    		System.out.println("> Request options > URI host: " + exchange.getRequestOptions().getURIHost());
	    		
	    		// separates the URIs
	    		LinkedHashMap<String, String> filters = new LinkedHashMap<>();
		    	List<String> params = exchange.getRequestOptions().getURIQueries();
		    	
		    	System.out.println("Parameters: " + params);
		    	System.out.println("\n*****");
		    	
		    	for(String uriParam : params){
		    		String[] keyValue = uriParam.split("=");
		    		filters.put(keyValue[0].trim(), keyValue[1].trim());
		    	}
		    	
		    	System.out.println("filters: " + filters);
		    	
		    	// This filters by all children devices
	    		Collection<Resource> filtered = filterDevices(filters);
	    		exchange.respond(ResponseCode.VALID, new Gson().toJson(convertResourceToDevice(filtered)), MediaTypeRegistry.APPLICATION_JSON);
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    	
	    	System.out.println("=======================");
	    }
	    
	    /**
	     * @param resources
	     * @return
	     */
	    private List<Device> convertResourceToDevice(Collection<Resource> resources){
	    	List<Device> devices = new ArrayList<>();
	    	Iterator<Resource> iterator = resources.iterator();
	    	while (iterator.hasNext()) {
				Resource resource = (Resource) iterator.next();
				Iterator<String> attrKeySet = resource.getAttributes().getAttributeKeySet().iterator();
				
				Device device = new Device();
				device.setUid(resource.getName());
				LinkedHashMap<String, String> context = new LinkedHashMap<>();
				while (attrKeySet.hasNext()) {
					String key = (String) attrKeySet.next();
					String value = resource.getAttributes().getAttributeValues(key).get(0).trim();
					
					if(!value.isEmpty()){
						if(key.equals("if")){
							device.setType(value);
						}else if(key.equals("ct")){
							device.setContextType(Integer.valueOf(value));
						}else if(key.equals("rt")){
							device.setResourceType(value);
						}else if(key.equals("ip")){
							device.setIp(value);
						}else{
							context.put(key, value);
						}
					}
				}
				device.setContext(context);
				devices.add(device);
			}
	    	return devices;
	    }
	    
	    /**
	     * @param filters
	     * @return
	     */
	    private Collection<Resource> filterDevices(HashMap<String, String> filters){
	    	Collection<Resource> res = new ArrayList<Resource>(getChildren());
	    	
	    	Iterator<Resource> iterator = res.iterator();
	    	while (iterator.hasNext()) {
				Resource resource = (Resource) iterator.next();
				Iterator<String> attrKeySet = resource.getAttributes().getAttributeKeySet().iterator();
				boolean flagRemove = false;
				while (attrKeySet.hasNext()) {
					String key = (String) attrKeySet.next();
					String value = resource.getAttributes().getAttributeValues(key).get(0).trim();
					if(filters.get(key) != null && !filters.get(key).equalsIgnoreCase(value)){
						flagRemove = true;
						break;
					}
				}
				if(flagRemove) iterator.remove();
			}
	    	
	    	//Filter by proximity (?proximity=radius,lat,long)
	    	if(filters.get("proximity") != null && !filters.get("proximity").isEmpty()){
	    		String[] fValues = filters.get("proximity").split(",");
	    		try{
	    			Double radius = Double.valueOf(fValues[0]);
	    			Double fLat = Double.valueOf(fValues[1]);
	    			Double fLong = Double.valueOf(fValues[2]);
	    			
	    			Iterator<Resource> newIterator = res.iterator();
	    			while (newIterator.hasNext()) {
	    				Resource resource = (Resource) newIterator.next();
	    				try{
	    					Double deviceLat = Double.valueOf(resource.getAttributes().getAttributeValues("latitude").get(0));
	    					Double deviceLong = Double.valueOf(resource.getAttributes().getAttributeValues("longitude").get(0));
	    					
	    					if(Utils.distance(fLat, deviceLat, fLong, deviceLong) > radius) newIterator.remove();
	    				}catch(Exception nPP){
	    					newIterator.remove();
	    				}
	    			}
	    		}catch(Exception ex){ /* Problems with filter. So, ignore it! */ }
	    	}
	    	
	    	return res;
	    }
	}

}