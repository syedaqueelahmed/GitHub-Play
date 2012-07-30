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
		URL url=new URL("https://api.github.com/users/"+uname+"/repos");
		HttpURLConnection con=(HttpURLConnection) url.openConnection();
		InputStream is=con.getInputStream();
		StringBuilder s=new StringBuilder("");
		
		while((i=is.read())!=-1){
			s.append((char)i);
		}
		if(s.toString().equals("[]")){
			return ok(views.html.show.render(new Html("<br /><br />User has no repos.<br /><br /><br /><a href=\"/\">Back</a>"), uname));
		}
		JSONArray ar=new JSONArray(s.toString());
		JSONObject o;
		
		html="<div align=\"center\"><table cellspacing=\"20\" style=\"text-align: center;\">" +
				"<tr>" +
					"<th>Repo</th>" +
					"<th>Watchers</th>" +
					"<th>Forks</th>" +
					"<th>Open Issues</th>" +
					"<th>Language</th>" +
					"<th>Home Page</th>" +
				"</tr>";
//		html="<ul style=\"position:center; list-style-type: none;\">";
		for(i=0; i<ar.length();i++){
			o=ar.getJSONObject(i);
			html=html.concat("<tr>");
				html=html.concat("<td><a target=\"_blank\" href="+o.getString("html_url")+">"+o.getString("name")+"</a></td>");
				html=html.concat("<td>"+o.getString("watchers_count")+"</td>");
				html=html.concat("<td>"+o.getString("forks_count")+"</td>");
				html=html.concat("<td>"+o.getString("open_issues_count")+"</td>");
				html=html.concat("<td>"+o.getString("language")+"</td>");
				html=html.concat("<td>"+o.getString("homepage")+"</td>");
			html=html.concat("</tr>");
		}
//		html=html.concat("</ul>");
		html=html.concat("</table></div>");
		
	  } catch (MalformedURLException e) {
			e.printStackTrace();
	  } catch (IOException e) {
		  	uname="";
		  	html="Redirecting back in 5 sec...";
		  	e.printStackTrace();
	  } catch (JSONException e) {
		e.printStackTrace();
	  }
	  
	  return ok(views.html.show.render(new Html(html),uname));
  }
  
}