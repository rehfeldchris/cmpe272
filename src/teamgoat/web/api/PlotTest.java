package teamgoat.web.api;

import java.io.File;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.IOException;
import java.security.AccessController;
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

import teamgoat.data.DataProviderFactory;
import teamgoat.data.Db2BigSqlDataProvider;
import teamgoat.data.SqliteDataProvider;
import teamgoat.data.UserLocationDataProvider;
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
		

		setup();
		UserLocationDataProvider dataProvider = DataProviderFactory.singleton();
		
		List<UserLocationSnapshot> results;
		
		try {
			results = dataProvider.getUsersWithinTimeRange(dateTime, seconds);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
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
	
	private void setup() {
        String s = getServletContext().getRealPath("/");
		if (s.endsWith("/.") || s.endsWith("\\.")) {
			s = s.substring(0, s.length() -2);
		}
        SqliteDataProvider.cwd = s;
	}
	
	
	public boolean checkFileCanRead(File file){
	    if (!file.exists()) 
	        return false;
	    if (!file.canRead())
	        return false;
	    try {
	        FileReader fileReader = new FileReader(file.getAbsolutePath());
	        fileReader.read();
	        fileReader.close();
	    } catch (Exception e) {
	        return false;
	    }
	    return true;
	}
	
}
