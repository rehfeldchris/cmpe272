package teamgoat.entity;

import java.util.Date;

/**
 * contains info about a users location at some specific instant in time.
 * 
 * @author chrehfel
 */
public class UserLocationSnapshot {
	private User user;
	private double lat;
	private double lng;
	private Date timestamp;
	
	public UserLocationSnapshot(User user, double lat, double lng, Date timestamp) {
		this.user = user;
		this.lat = lat;
		this.lng = lng;
		this.timestamp = timestamp;
	}
	
	public User getUser() {
		return user;
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
