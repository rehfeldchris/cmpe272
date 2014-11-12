package teamgoat.web.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import teamgoat.entity.User;
import teamgoat.entity.UserLocationSnapshot;

@WebServlet(name = "JobProcessor", urlPatterns = { "/JobProcessor" })
public class JobProcessor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public JobProcessor() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String json = getGson().toJson(new ApiResponse(getMockData()));
		response.setContentType("application/javascript");
		response.getWriter().write(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String originalInfectedUserId = request.getParameter("originalInfectedUserId");
		Integer maxNodeHopsFromOrigin = getParamAsInt(request, "maxNodeHopsFromOrigin");
		Integer minInfectionMinutes = getParamAsInt(request, "minInfectionMinutes");
		Integer minInfectionRangeYards = getParamAsInt(request, "minInfectionRangeYards");
		
		try {
			maxNodeHopsFromOrigin = Integer.parseInt(request.getParameter("maxNodeDistanceFromOrigin"));
		} catch (NumberFormatException e) {
			
		}
		
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
	
	private int getInfectedUserId(HttpServletRequest request) {
		// Try to parse the input as an integer first
		Integer userId = getParamAsInt(request, "originalInfectedUser");
		if (userId != null) {
			return userId;
		}
		
		// If it failed, they probably entered a persons name, so we try to translate that to an id.
		String name = request.getParameter("originalInfectedUser");
		return findUserIdByName(name);
	}
	
	private Integer findUserIdByName(String name)
	{
		// todo
		return null;
	}
	
	private List<UserLocationSnapshot> getMockData() {
		User chris = new User("Chris", 1);
		User carita = new User("Carita", 2);
		User amy = new User("Amy", 3);
		User thong = new User("Thong", 4);
		List<UserLocationSnapshot> records = new ArrayList<>();
		records.addAll(mockRecordsForUser(chris));
		records.addAll(mockRecordsForUser(carita));
		records.addAll(mockRecordsForUser(amy));
		records.addAll(mockRecordsForUser(thong));
		return records;
	}
	
	private List<UserLocationSnapshot> mockRecordsForUser(User user) {
		List<UserLocationSnapshot> records = new ArrayList<>();
		Random rand = new Random();
		double lat = 37.7833 + rand.nextDouble();
		double lng = 122.4167 + rand.nextDouble();
		for (int i = 0; i < 5; i++) {
			records.add(new UserLocationSnapshot(
					user, 
					lat + rand.nextDouble(), 
					lng + rand.nextDouble(), 
					makeDate(i)
			));
		}
		return records;
	}
	
	private Date makeDate(int minutesToAdd) {
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss", Locale.ENGLISH);
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateParser.parse("2014-11-12 12:33:44"));
			cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minutesToAdd);
			return cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Gson getGson() {
		return new GsonBuilder()
		.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX") // use ISO 8601 format for serializing dates because js supports it well.
		.setPrettyPrinting() 
		.create();
	}

}
