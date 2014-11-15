package teamgoat.entity;

import org.joda.time.DateTime;

/**
 * contains info about a users location at some instant in time.
 * 
 * @author chrehfel
 */
public class UserLocationSnapshot {
	private User user;
	private TemporalLocation temporalLocation;
	
	public UserLocationSnapshot(User user, TemporalLocation temporalLocation) {
		this.user = user;
		this.temporalLocation = temporalLocation;
	}
	
	public User getUser() {
		return user;
	}
	
	public double getLat() {
		return temporalLocation.getLat();
	}
	
	public double getLng() {
		return temporalLocation.getLng();
	}
	
	public DateTime getTimestamp() {
		return temporalLocation.getTimestamp();
	}

	public TemporalLocation getTemporalLocation() {
		return temporalLocation;
	}
}
