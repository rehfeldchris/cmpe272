package teamgoat.data;

import java.util.List;

import org.joda.time.DateTime;

import teamgoat.entity.TemporalLocation;
import teamgoat.entity.User;
import teamgoat.entity.UserLocationSnapshot;

public interface UserLocationDataProvider {
	public List<UserLocationSnapshot> getUsersWithinRange(TemporalLocation temporalLocation, double maxRangeInMeters) throws DataAccessException;
	public UserLocationSnapshot getLocation(User user, DateTime instant) throws DataAccessException;
	public User getUser(int userId) throws DataAccessException;
	public UserLocationSnapshot getLocationAtArbitraryTime(User user) throws DataAccessException;
	public List<UserLocationSnapshot> getUsersWithinTimeRange(DateTime dateTime, int seconds) throws Exception;
	public boolean testConnection();
}