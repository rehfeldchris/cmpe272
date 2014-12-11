package teamgoat.web.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import teamgoat.data.Db2BigSqlDataProvider;
import teamgoat.entity.UserLocationSnapshot;

@WebServlet(name = "PlotTest", urlPatterns = { "/PlotTest" })
public class PlotTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PlotTest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DateTime dateTime = getDateTime(request, "dateTime");
		int seconds = Integer.parseInt(request.getParameter("seconds"));
		
		Db2BigSqlDataProvider dataProvider = new Db2BigSqlDataProvider();
		
		List<UserLocationSnapshot> results = dataProvider.getUsersWithinTimeRange(dateTime, seconds);
		
		String json = getGson().toJson(new PlotTestResponse(results));
		response.setContentType("application/javascript");
		response.getWriter().write(json);
	}
	
	private DateTime getDateTime(HttpServletRequest request, String isoDateString) {
		return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").parseDateTime(request.getParameter(isoDateString));
	}
	
	private Gson getGson() {
		GsonBuilder builder = Converters.registerDateTime(new GsonBuilder());
		return builder
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX") // use ISO 8601 format for serializing dates because js supports it well.
			.setPrettyPrinting()
			.serializeNulls()
			.create();
	}
	
}
