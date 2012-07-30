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
		JSONArray ar=new JSONArray(s.toString());
		JSONObject o;

		html="<ul style=\"position:absolute; left:40%;\">";
		for(i=0; i<ar.length();i++){
			o=ar.getJSONObject(i);
			html=html.concat("<li><a href="+o.getString("html_url")+">"+o.getString("name")+"</a></li>");
		}
		html=html.concat("</ul>");
		System.out.println(html);
		
	  } catch (MalformedURLException e) {
			e.printStackTrace();
	  } catch (IOException e) {
		e.printStackTrace();
	  } catch (JSONException e) {
		e.printStackTrace();
	  }
	  
	  return ok(views.html.show.render(new Html(html),uname));
  }
  
}