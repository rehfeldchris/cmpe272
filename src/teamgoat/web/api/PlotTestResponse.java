package teamgoat.web.api;

import java.util.List;

import teamgoat.entity.UserLocationSnapshot;

/**
 * defines the structure we use to deliver json to the client.
 * 
 * @author chrehfel
 */
public class PlotTestResponse {
	private List<UserLocationSnapshot> userLocationSnapshots;

	public PlotTestResponse(List<UserLocationSnapshot> userLocationSnapshots) {
		this.userLocationSnapshots = userLocationSnapshots;
	}

	public List<UserLocationSnapshot> getUserLocationSnapshots() {
		return userLocationSnapshots;
	}
}
