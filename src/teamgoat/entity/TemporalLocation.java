package teamgoat.entity;

import org.joda.time.DateTime;

/**
 * describes a location at some instant in time.
 * 
 * @author chrehfel
 */
public class TemporalLocation {
	private double lat;
	private double lng;
	private DateTime timestamp;
	
	public TemporalLocation(double lat, double lng, DateTime timestamp) {
		this.lat = lat;
		this.lng = lng;
		this.timestamp = timestamp;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLng() {
		return lng;
	}
	
	public DateTime getTimestamp() {
		return timestamp;
	}
}
