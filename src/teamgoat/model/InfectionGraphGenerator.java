package teamgoat.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import teamgoat.entity.InfectedUserLocationSnapshot;
import teamgoat.entity.TemporalLocation;
import teamgoat.entity.User;
import teamgoat.entity.UserLocationSnapshot;

public class InfectionGraphGenerator {
	private GregorianCalendar time = new GregorianCalendar();
	private Set<User> infectedUserSet = new HashSet<>();
	private List<InfectedUserLocationSnapshot> infectedUsers = new ArrayList<>();
	private int maxHopsToFollow = 0;
	private int maxResultSize = 0;
	private double minimumRangeInMeters = 0;
	
	private List<UserLocationSnapshot> findAllWithinRange(TemporalLocation temporalLocation) {
		List<UserLocationSnapshot> userLocationSnapshotsWithinRange = new ArrayList<>();
		return userLocationSnapshotsWithinRange;
	}
	
	public List<InfectedUserLocationSnapshot> getInfectionGraph(InfectedUserLocationSnapshot origionalInfectedUser, int maxHopsToFollow, int maxResultSize, double minimumRangeInMeters) {
		time.setTime(origionalInfectedUser.getTimestamp());
		this.infectedUserSet = new HashSet<>();
		this.maxHopsToFollow = maxHopsToFollow;
		this.maxResultSize = maxResultSize;
		this.minimumRangeInMeters = minimumRangeInMeters;
		List<InfectedUserLocationSnapshot> users = new ArrayList<>();
		users.addAll(getUsersInfectedBy(origionalInfectedUser));
		
		return users;
	}
	
	private List<InfectedUserLocationSnapshot> getUsersInfectedBy(InfectedUserLocationSnapshot infectedUser) {
		List<InfectedUserLocationSnapshot> newlyInfectedUsers = new ArrayList<>();
		
		if (!canInfectOtherUsers(infectedUser)) {
			return newlyInfectedUsers;
		}
		
		for (UserLocationSnapshot userWithinRange : findAllWithinRange(infectedUser.getTemporalLocation())) {
			if (!infectedUserSet.contains(userWithinRange.getUser())) {
				newlyInfectedUsers.add(new InfectedUserLocationSnapshot(
					userWithinRange.getUser(), 
					userWithinRange.getTemporalLocation(), 
					infectedUser
				));
			}
		}
		
		return newlyInfectedUsers;
	}
	
	// If this infected user is too far in hops from the original infection, we don't allow them to infect any users.
	private boolean canInfectOtherUsers(InfectedUserLocationSnapshot infectedUser) {
		return infectedUser.distanceInHopsFromOrigin() <= maxHopsToFollow;
	}
	
	private void advanceTimeToNextTick() {
		time.set(Calendar.MINUTE, time.get(Calendar.MINUTE) + 5);
	}
}
