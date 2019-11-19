package great.android.cmu.ubiapp.model;

import java.util.LinkedHashMap;

public class Device {

    /** Unique identification. E.g. stemp01 */
    private String uid;
    /** Types: actuator or sensor */
    private String type;
    /** Resource type: e.g.: temperature, pressure, lightness, print */
    private String resourceType;
    /** Context Type: e.g.: string, integer, json, ... */
    private int contextType;
    /** Device IP */
    private String ip;
    /** Device context. E.g.: <"temperature", "29ºC"> */
    private LinkedHashMap<String, String> context;

    public Device(){

    }

    /**
     * @param uid
     * @param type
     * @param resourceType
     * @param contextType
     * @param ip
     */
    public Device(String uid, String type, String resourceType, int contextType, String ip) {
        this.uid = uid;
        this.type = type;
        this.resourceType = resourceType;
        this.contextType = contextType;
        this.ip = ip;
        this.context = new LinkedHashMap<>();
    }

    /**
     * @param variable
     * @param value
     */
    public void addContext(String variable, String value){
        this.context.put(variable, value);
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * @param resourceType the resourceType to set
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * @return the contextType
     */
    public int getContextType() {
        return contextType;
    }

    /**
     * @param contextType the contextType to set
     */
    public void setContextType(int contextType) {
        this.contextType = contextType;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the context
     */
    public LinkedHashMap<String, String> getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(LinkedHashMap<String, String> context) {
        this.context = context;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Device [uid = " + uid + "]\n");
        buffer.append("\t- type = " + type + "\n");
        buffer.append("\t- resourceType = " + resourceType + "\n");
        buffer.append("\t- contextType = " + contextType + "\n");
        buffer.append("\t- ip = " + ip);
        return buffer.toString();
    }
}
