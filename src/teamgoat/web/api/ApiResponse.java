package teamgoat.web.api;

import java.util.List;

import teamgoat.entity.InfectedUserLocationSnapshot;

/**
 * defines the structure we use to deliver json to the client.
 * 
 * @author chrehfel
 */
public class ApiResponse {
	private List<InfectedUserLocationSnapshot> userLocationSnapshots;

	public ApiResponse(List<InfectedUserLocationSnapshot> userLocationSnapshots) {
		this.userLocationSnapshots = userLocationSnapshots;
	}

	public List<InfectedUserLocationSnapshot> getUserLocationSnapshots() {
		return userLocationSnapshots;
	}
}
