/**
 * 
 */
package great.android.cmu.ubiapp.model;

/**
 * @author PedroAlmir
 */
public class DeviceAction {
	/** DeviceAction type: vibrate or light */
	private String type;
	/** 
	 * DeviceAction duration in milliseconds.
	 * If duration is < 0 the effect will be just <turn on>
	 * If duration is = 0 the effect will be just <turn off>
	 *  */
	private int duration;
	
	/** Default constructor */
	public DeviceAction() {
		
	}
	
	/**
	 * @param type
	 * @param duration
	 */
	public DeviceAction(String type, int duration) {
		this.type = type;
		this.duration = duration;
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
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeviceAction [type=" + type + ", duration=" + duration + "]";
	}
}
