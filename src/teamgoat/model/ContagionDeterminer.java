package teamgoat.model;

import org.joda.time.DateTime;

import teamgoat.entity.InfectedUserLocationSnapshot;

public interface ContagionDeterminer {
	public boolean isContagious(InfectedUserLocationSnapshot infectedUserLocationSnapshot, DateTime currentTime);
}
