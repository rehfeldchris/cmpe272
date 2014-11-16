package teamgoat.data;

import java.sql.Statement;
import java.util.List;

import org.joda.time.DateTime;

import teamgoat.entity.TemporalLocation;
import teamgoat.entity.User;
import teamgoat.entity.UserLocationSnapshot;

public class BuzzwordPoweredDataProvider implements UserLocationDataProvider {
	private Statement userlocatiponStmt;

	public List<UserLocationSnapshot> getUsersWithinRange(TemporalLocation temporalLocation, double maxRangeInMeters) throws DataAccessException {
		return null;
	}

	public UserLocationSnapshot getLocation(User user, DateTime instant) throws DataAccessException {
		return null;
	}

}
