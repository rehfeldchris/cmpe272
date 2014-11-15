package teamgoat.entity;

import java.util.Date;

/**
 * describes a location at some instant in time.
 * 
 * @author chrehfel
 */
public class TemporalLocation {
	private double lat;
	private double lng;
	private Date timestamp;
	
	public TemporalLocation(double lat, double lng, Date timestamp) {
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
	
	public Date getTimestamp() {
		return timestamp;
	}
}
