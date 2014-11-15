package teamgoat.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.MutableDateTime;

import teamgoat.data.UserLocationDataProvider;
import teamgoat.entity.InfectedUserLocationSnapshot;
import teamgoat.entity.User;
import teamgoat.entity.UserLocationSnapshot;

public class InfectionGraphGenerator {
	private UserLocationDataProvider dataProvider;
	private MutableDateTime currentTime;
	private DateTime endTime;
	private Duration tickDuration = Duration.standardSeconds(5);
	private Map<User, InfectedUserLocationSnapshot> infectedUsers = new LinkedHashMap<>();
	private int maxResultSize = 0;
	private double maximumInfectionRangeInMeters = 0;
	ContagionDeterminer contagionDeterminer;
	
	public InfectionGraphGenerator(UserLocationDataProvider dataProvider, MutableDateTime currentTime, Duration maxDurationOfInfectionSpreading, int maxResultSize, double maximumInfectionRangeInMeters, ContagionDeterminer contagionDeterminer) {
		this.dataProvider = dataProvider;
		this.currentTime = currentTime;
		endTime = new DateTime(currentTime).withDurationAdded(maxDurationOfInfectionSpreading, 1);
		this.maxResultSize = maxResultSize;
		this.maximumInfectionRangeInMeters = maximumInfectionRangeInMeters;
		this.contagionDeterminer = contagionDeterminer;
	}

	private List<UserLocationSnapshot> findAllCurrentlyWithinRangeOfUser(User user) {
		UserLocationSnapshot currentLocation = getCurrentLocation(user);

		List<UserLocationSnapshot> userLocationSnapshotsWithinRange = new ArrayList<>();
		return userLocationSnapshotsWithinRange;
	}
	
	private UserLocationSnapshot getCurrentLocation(User user) {
		return dataProvider.getLocation(user, currentTime.toDateTime());
	}
	
	public List<InfectedUserLocationSnapshot> getInfectionGraph(InfectedUserLocationSnapshot origionalInfectedUser) {
		currentTime = new MutableDateTime(origionalInfectedUser.getTimestamp());
		
		List<InfectedUserLocationSnapshot> users = new ArrayList<>();
		while (shouldContinueInfectingMoreUsers()) {
			
			// Loop over all the currently infected users, finding any new users they infect at this time.
			// We make a copy of the list so that we can add more to it while iterating it.
			for (User contagiousUser : getAllUsersWhoCanInfectOthers()) {
				for (InfectedUserLocationSnapshot newlyInfectedUserSnapshot : getUsersInfectedBy(contagiousUser)) {
					infectedUsers.put(newlyInfectedUserSnapshot.getUser(), newlyInfectedUserSnapshot);
				}
			}
			
			advanceTimeToNextTick();
		}

		return users;
	}
	
	private List<InfectedUserLocationSnapshot> getUsersInfectedBy(User infectedUser) {
		List<InfectedUserLocationSnapshot> newlyInfectedusers = new ArrayList<>();
		for (UserLocationSnapshot snapshot : findAllCurrentlyWithinRangeOfUser(infectedUser)) {
			if (!infectedUsers.containsKey(snapshot.getUser())) {
				newlyInfectedusers.add(new InfectedUserLocationSnapshot(
					snapshot.getUser(), 
					snapshot.getTemporalLocation(), 
					infectedUsers.get(infectedUser))
				);
			}
		}
		return newlyInfectedusers;
	}
	
	private boolean shouldContinueInfectingMoreUsers() {
		return infectedUsers.size() < maxResultSize
			&& currentTime.isBefore(endTime);
	}
	
	private List<User> getAllUsersWhoCanInfectOthers() {
		List<User> users = new ArrayList<>();
		for (InfectedUserLocationSnapshot infectedUser : infectedUsers.values()) {
			if (canInfectOtherUsers(infectedUser)) {
				users.add(infectedUser.getUser());
			}
		}
		return users;
	}
	
	private boolean canInfectOtherUsers(InfectedUserLocationSnapshot infectedUser) {
		return isContagious(infectedUser);
	}
	
	private boolean isContagious(InfectedUserLocationSnapshot infectedUser) {
		return contagionDeterminer.isContagious(infectedUser, currentTime.toDateTime());
	}
	
	private void advanceTimeToNextTick() {
		currentTime.add(tickDuration, 1);
	}
}
