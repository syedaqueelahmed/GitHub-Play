package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.api.templates.Html;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
  public static Result index() {
    return ok(index.render());
  }
  
  public static Result show(String uname){
	  String html="";
	  int i=0;
	  try {
		URL url=new URL("https://api.github.com/users/"+uname+"/repos?per_page=100");
		HttpURLConnection con=(HttpURLConnection) url.openConnection();
		InputStream is=con.getInputStream();
		StringBuilder s=new StringBuilder("");
		
		while((i=is.read())!=-1){
			s.append((char)i);
		}
		if(s.toString().equals("[]")){
			return ok(views.html.show.render(new Html("<br /><br /><br /><br /><br /><h4>User has no repos.</h4><br /><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>"), uname));
		}
		JSONArray ar=new JSONArray(s.toString());
		JSONObject o;
		
		html="";
		for(i=0; i<ar.length();i++){
			o=ar.getJSONObject(i);
			html=html.concat("<div class=\"container\">");
				html=html.concat("<div class=\"row-fluid\">");
					html=html.concat("<h3><a href=\""+o.getString("html_url")+"\" target=\"_blank\">"+o.getString("name")+"</a></h3><br>");
				html=html.concat("</div>");
				html=html.concat("<div class=\"row-fluid\">");
					html=html.concat("<div class=\"span3 thumbnail\">");
						html=html.concat("<h4>Watchers</h4><br>" +
								"<p>"+o.getString("watchers_count")+"</p></div>");
					html=html.concat("<div class=\"span3 thumbnail\">");
						html=html.concat("<h4>Forks</h4><br>" +
								"<p>"+o.getString("forks_count")+"</p></div>");
					html=html.concat("<div class=\"span3 thumbnail\">");
						html=html.concat("<h4>Open Issues</h4><br>" +
								"<p>"+o.getString("open_issues_count")+"</p></div>");
					html=html.concat("<div class=\"span3 thumbnail\">");
						html=html.concat("<h4>Language</h4><br>" +
								"<p>"+o.getString("language")+"</p></div>");
			html=html.concat("</div></div><br><hr><br>");
		}

	  } catch (MalformedURLException e) {
			e.printStackTrace();
	  } catch (IOException e) {
		  	uname="";
		  	html="";
		  	e.printStackTrace();
	  } catch (JSONException e) {
		e.printStackTrace();
	  }
	  
	  return ok(views.html.show.render(new Html(html),uname));
  }
  
}