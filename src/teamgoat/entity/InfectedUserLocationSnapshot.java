package teamgoat.entity;

/**
 * contains info about a users location at the instant they got infected. It also specifiies the user
 * who infected them.
 * 
 * @author chrehfel
 */
public class InfectedUserLocationSnapshot extends UserLocationSnapshot {
	private InfectedUserLocationSnapshot infectingUser;
	
	public InfectedUserLocationSnapshot(User user, TemporalLocation temporalLocation, InfectedUserLocationSnapshot infectingUser) {
		super(user, temporalLocation);
		this.infectingUser = infectingUser;
	}

	public InfectedUserLocationSnapshot getInfectingUser() {
		return infectingUser;
	}
	
	public int distanceInHopsFromOrigin() {
		int hops = 0;
		InfectedUserLocationSnapshot cursor = this;
		while (cursor.infectingUser != null && cursor.infectingUser != this) {
			hops++;
			cursor = cursor.infectingUser;
		}
		return hops;
	}

	public String toString() {
		return "InfectedUserLocationSnapshot [infectingUser=" + infectingUser+ ", toString()=" + super.toString() + "]";
	}
}
