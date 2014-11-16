package teamgoat.data;

import java.util.List;

import org.joda.time.DateTime;

import teamgoat.entity.TemporalLocation;
import teamgoat.entity.User;
import teamgoat.entity.UserLocationSnapshot;

public interface UserLocationDataProvider {
	public List<UserLocationSnapshot> getUsersWithinRange(TemporalLocation temporalLocation, double maxRangeInMeters) throws DataAccessException;
	public UserLocationSnapshot getLocation(User user, DateTime instant) throws DataAccessException;
}