/**
 * 
 */
package great.android.cmu.ubiapp.model;

import java.util.List;

/**
 * @author PedroAlmir
 *
 */
public class DeviceActionMessage {
	/** Communication token */
	private String cToken;
	/** List of actions to execute in device */
	private List<DeviceAction> actions;
	/** List of IPs to actuate */
	private List<String> ips;
	
	/** Default constructor */
	public DeviceActionMessage() {}
	
	/**
	 * @param cToken
	 * @param actions
	 * @param ips
	 */
	public DeviceActionMessage(String cToken, List<DeviceAction> actions, List<String> ips) {
		this.cToken = cToken;
		this.actions = actions;
		this.ips = ips;
	}


	/**
	 * @return the cToken
	 */
	public String getcToken() {
		return cToken;
	}
	/**
	 * @param cToken the cToken to set
	 */
	public void setcToken(String cToken) {
		this.cToken = cToken;
	}
	/**
	 * @return the actions
	 */
	public List<DeviceAction> getActions() {
		return actions;
	}
	/**
	 * @param actions the actions to set
	 */
	public void setActions(List<DeviceAction> actions) {
		this.actions = actions;
	}

	/**
	 * @return the ips
	 */
	public List<String> getIps() {
		return ips;
	}

	/**
	 * @param ips the ips to set
	 */
	public void setIps(List<String> ips) {
		this.ips = ips;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeviceActionMessage [cToken=" + cToken + ", actions=" + actions + ", ips=" + ips + "]";
	}
}
