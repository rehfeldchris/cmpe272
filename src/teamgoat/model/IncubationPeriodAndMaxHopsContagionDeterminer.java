package teamgoat.model;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import teamgoat.entity.InfectedUserLocationSnapshot;

public class IncubationPeriodAndMaxHopsContagionDeterminer implements ContagionDeterminer {
	private Duration incubationPeriod;
	private int maxHopsFromOrigionInfection;
	
	public IncubationPeriodAndMaxHopsContagionDeterminer(Duration incubationPeriod, int maxHopsFromOrigionInfection) {
		this.incubationPeriod = incubationPeriod;
		this.maxHopsFromOrigionInfection = maxHopsFromOrigionInfection;
	}

	public boolean isContagious(InfectedUserLocationSnapshot infectedUser, DateTime currentTime) {
		return infectedUser.distanceInHopsFromOrigin() < maxHopsFromOrigionInfection
			&& infectedUser.getTimestamp().withDurationAdded(incubationPeriod, 1).isBefore(currentTime)
		;
	}
}
