package adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

public class UtilDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
	@Override //把Java对象--->JSON字符串
	public JsonElement serialize(Date src, Type arg1,
			JsonSerializationContext arg2) {
		if(null == src){

			return null;

		}


		
		return new JsonPrimitive(src.getTime());
	}
	
	@Override //把JSON字符串--->Java对象
	public Date deserialize(JsonElement src, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		if(src.isJsonNull()){
			return null;
		}
		
		long ms = src.getAsLong();
		
		return new Date(ms);
	}
}
