package tools;

import java.util.Date;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import adapter.UtilDateTypeAdapter;


public class GsonHelper {
	private static Gson gson;
	
	public synchronized static Gson getGson(){
		if(gson == null){
			gson = new GsonBuilder()
				.registerTypeAdapter(Date.class, new UtilDateTypeAdapter())
				.create();
		}
		
		return gson;
	}
}