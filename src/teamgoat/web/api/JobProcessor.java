package teamgoat.web.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import teamgoat.data.DataAccessException;
import teamgoat.data.DataProviderFactory;
import teamgoat.data.Db2BigSqlDataProvider;
import teamgoat.data.SqliteDataProvider;
import teamgoat.data.UserLocationDataProvider;
import teamgoat.entity.InfectedUserLocationSnapshot;
import teamgoat.entity.User;
import teamgoat.entity.UserLocationSnapshot;
import teamgoat.model.IncubationPeriodAndMaxHopsContagionDeterminer;
import teamgoat.model.InfectionGraphGenerator;

@WebServlet(name = "JobProcessor", urlPatterns = { "/JobProcessor" })
public class JobProcessor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public JobProcessor() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer originalInfectedUserId = getParamAsInt(request, "originalInfectedUserId");
		Integer maxNodeHopsFromOrigin = getParamAsInt(request, "maxNodeHopsFromOrigin");
		Integer maxResultSize = getParamAsInt(request, "maxResultSize");
		Double maximumInfectionRangeInMeters = getParamAsDouble(request, "minInfectionRangeYards");
		Duration maxTimeOfInfectionSpreading = getDuration(request.getParameter("maxTimeOfInfectionSpreading"));
		Duration incubationTime = getDuration(request.getParameter("incubationTime"));
		
		setup();
		UserLocationDataProvider dataProvider = DataProviderFactory.singleton();

		User user = dataProvider.getUser(originalInfectedUserId);
		UserLocationSnapshot infectionStartPoint = null;
		try {
			infectionStartPoint = dataProvider.getLocation(user, getDateTime(request, "startTime"));
		} catch (Exception e) {}

		// If no record was found, try to find a record for the userid at any time we can. This makes using the tool easier.
		if (infectionStartPoint == null) {
			infectionStartPoint = dataProvider.getLocationAtArbitraryTime(user);
		}
		
		List<InfectedUserLocationSnapshot> results = getResults(infectionStartPoint, maxTimeOfInfectionSpreading, incubationTime, maxNodeHopsFromOrigin, maxResultSize, maximumInfectionRangeInMeters);
		
		System.out.printf("Produced %d infected\n", results.size());
		String json = getGson().toJson(new ApiResponse(results));
		response.setContentType("application/json");
		response.getWriter().write(json);
	}
	
	private Duration getDuration(String duration) {
		PeriodFormatter hoursMinutes = new PeriodFormatterBuilder()
	     .appendHours()
	     .appendSeparator(":")
	     .appendMinutes()
	     .toFormatter();
		return hoursMinutes.parsePeriod(duration).toStandardDuration();
	}
	
	private DateTime getDateTime(HttpServletRequest request, String isoDateString) {
		return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").parseDateTime(request.getParameter(isoDateString).trim());
	}

	private Integer getParamAsInt(HttpServletRequest request, String requestParameterName) {
		try {
			return Integer.parseInt(request.getParameter(requestParameterName).trim());
		} catch (Exception e) {
			return null;
		}
	}

	private Double getParamAsDouble(HttpServletRequest request, String requestParameterName) {
		try {
			return Double.parseDouble(request.getParameter(requestParameterName).trim());
		} catch (Exception e) {
			return null;
		}
	}
	
	private Gson getGson() {
		GsonBuilder builder = Converters.registerDateTime(new GsonBuilder());
		return builder
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX") // use ISO 8601 format for serializing dates because js supports it well.
			.setPrettyPrinting()
			.serializeNulls()
			.create();
	}
	
	private List<InfectedUserLocationSnapshot> getResults(UserLocationSnapshot origionalInfectedUser, Duration maxTimeOfInfectionSpreading, Duration incubationTime, int maxContagionHopsFromOrigin, int maxResultSize, double maximumInfectionRangeInMeters) {
		UserLocationDataProvider dataProvider = DataProviderFactory.singleton();
		
		InfectionGraphGenerator generator = new InfectionGraphGenerator(
			dataProvider, 
			maxTimeOfInfectionSpreading, 
			maxResultSize, 
			maximumInfectionRangeInMeters, 
			new IncubationPeriodAndMaxHopsContagionDeterminer(incubationTime, maxContagionHopsFromOrigin)
		);
		
		return generator.getInfectionGraph(new InfectedUserLocationSnapshot(
			origionalInfectedUser.getUser(), 
			origionalInfectedUser.getTemporalLocation(), 
			null
		));
	}
	
	private void setup() {
        String s = getServletContext().getRealPath(".");
        SqliteDataProvider.cwd = s;
	}
}
