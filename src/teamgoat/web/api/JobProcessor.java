package teamgoat.web.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

import teamgoat.data.BuzzwordPoweredDataProvider;
import teamgoat.data.UserLocationDataProvider;
import teamgoat.entity.InfectedUserLocationSnapshot;
import teamgoat.entity.TemporalLocation;
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
		Double maximumInfectionRangeInMeters = (double) 1000000;//getParamAsDouble(request, "minInfectionRangeYards");
		Duration maxTimeOfInfectionSpreading = getDuration(request.getParameter("maxTimeOfInfectionSpreading"));
		Duration incubationTime = getDuration(request.getParameter("incubationTime"));
		
		
		BuzzwordPoweredDataProvider dataProvider = new BuzzwordPoweredDataProvider();
		
		System.out.println(dataProvider.getUsersTest());
		User user = dataProvider.getUser(originalInfectedUserId);
		UserLocationSnapshot infectionStartPoint = dataProvider.getLocation(user, getDateTime(request, "startTime"));
		
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
		return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").parseDateTime(request.getParameter(isoDateString));
	}

	private Integer getParamAsInt(HttpServletRequest request, String requestParameterName) {
		try {
			return Integer.parseInt(request.getParameter(requestParameterName));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private Double getParamAsDouble(HttpServletRequest request, String requestParameterName) {
		try {
			return Double.parseDouble(request.getParameter(requestParameterName));
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	private List<InfectedUserLocationSnapshot> getMockData() {
		User chris = new User("Chris", 1);
		User carita = new User("Carita", 2);
		User amy = new User("Amy", 3);
		User liping = new User("Liping", 4);
		List<InfectedUserLocationSnapshot> records = new ArrayList<>();
		records.addAll(mockRecordsForUser(chris));
		records.addAll(mockRecordsForUser(carita));
		records.addAll(mockRecordsForUser(amy));
		records.addAll(mockRecordsForUser(liping));
		return records;
	}
	
	private List<InfectedUserLocationSnapshot> mockRecordsForUser(User user) {
		List<InfectedUserLocationSnapshot> records = new ArrayList<>();
		Random rand = new Random();
		double lat = 37.7833 + rand.nextDouble();
		double lng = 122.4167 + rand.nextDouble();
		for (int i = 0; i < 5; i++) {
			records.add(new InfectedUserLocationSnapshot(
					user,
					new TemporalLocation(
						lat + rand.nextDouble(), 
						lng + rand.nextDouble(), 
						makeDate(i)
					),
					null
			));
		}
		return records;
	}
	
	private DateTime makeDate(int minutesToAdd) {
		return DateTimeFormat
				.forPattern("yyyy-MM-DD HH:mm:ss")
				.parseDateTime("2014-11-12 12:33:44")
				.withDurationAdded(
					Duration.standardMinutes(1), 
					minutesToAdd
				);
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
		UserLocationDataProvider dataProvider = new BuzzwordPoweredDataProvider();
		
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

}
