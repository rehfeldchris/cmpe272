package teamgoat.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.MutableDateTime;

import teamgoat.data.BuzzwordPoweredDataProvider;
import teamgoat.data.UserLocationDataProvider;
import teamgoat.entity.InfectedUserLocationSnapshot;
import teamgoat.entity.TemporalLocation;
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
	private ContagionDeterminer contagionDeterminer;
	private Duration maxDurationOfInfectionSpreading;
	
	public InfectionGraphGenerator(UserLocationDataProvider dataProvider, Duration maxDurationOfInfectionSpreading, int maxResultSize, double maximumInfectionRangeInMeters, ContagionDeterminer contagionDeterminer) {
		this.dataProvider = dataProvider;
		this.maxDurationOfInfectionSpreading = maxDurationOfInfectionSpreading;
		this.maxResultSize = maxResultSize;
		this.maximumInfectionRangeInMeters = maximumInfectionRangeInMeters;
		this.contagionDeterminer = contagionDeterminer;
	}

	private List<UserLocationSnapshot> findAllCurrentlyWithinRangeOfUser(User user) {
		UserLocationSnapshot currentLocation = getCurrentLocation(user);
		if (currentLocation == null) {
			return Collections.emptyList();
		}
		List<UserLocationSnapshot> usersWithinRange = dataProvider.getUsersWithinRange(
			currentLocation.getTemporalLocation(), 
			maximumInfectionRangeInMeters
		);
		
		System.out.printf("found %d records within range of an infected user at %s\n", usersWithinRange.size(), currentTime);	
		return usersWithinRange;
	}
	
	private UserLocationSnapshot getCurrentLocation(User user) {
		return dataProvider.getLocation(user, currentTime.toDateTime());
	}
	
	public List<InfectedUserLocationSnapshot> getInfectionGraph(InfectedUserLocationSnapshot origionalInfectedUser) {
		currentTime = new MutableDateTime(origionalInfectedUser.getTimestamp());
		endTime = new DateTime(currentTime).withDurationAdded(this.maxDurationOfInfectionSpreading, 1);
		infectedUsers.put(origionalInfectedUser.getUser(), origionalInfectedUser);
		
		List<InfectedUserLocationSnapshot> users = new ArrayList<>();
		while (shouldContinueInfectingMoreUsers()) {
			
			// Loop over all the currently infected users, finding any new users they infect at this time.
			// We make a copy of the list so that we can add more to it while iterating it.
			for (User contagiousUser : getAllUsersWhoCanInfectOthers()) {
				for (InfectedUserLocationSnapshot newlyInfectedUserSnapshot : getUsersInfectedBy(contagiousUser)) {
					recordInfectedUser(newlyInfectedUserSnapshot);
				}
			}
			
			advanceTimeToNextTick();
		}

		return new ArrayList<>(infectedUsers.values());
	}
	
	private void recordInfectedUser(InfectedUserLocationSnapshot newlyInfectedUserSnapshot) {
		System.out.println(newlyInfectedUserSnapshot);
		infectedUsers.put(newlyInfectedUserSnapshot.getUser(), newlyInfectedUserSnapshot);
	}
	
	private List<InfectedUserLocationSnapshot> getUsersInfectedBy(User infectedUser) {
		List<InfectedUserLocationSnapshot> newlyInfectedusers = new ArrayList<>();
		for (UserLocationSnapshot snapshot : findAllCurrentlyWithinRangeOfUser(infectedUser)) {
			if (!infectedUser.equals(snapshot.getUser()) && !infectedUsers.containsKey(snapshot.getUser())) {
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
