import java.io.FileInputStream;
import java.io.StringReader;
import java.sql.Blob;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class JSON {
	DataList dataList;
	JSONObject obj;
	
	JSONArray jsonArray = new JSONArray();
	DataList imageList;
	
	// 받아온 JSON 데이터를 GSON으로 파싱
	public DataList parseJSONFromGSON(String data){
		dataList = new DataList();
		dataList = new Gson().fromJson(data, dataList.getClass());
		return dataList;
	}
	
	// JSON 배열 만들기
	public String makeJSONArray(int count, String type, String BLUMAC, String name, String image){
		JSONArray array = new JSONArray();
		obj = new JSONObject();
		for(int i = 0; i<count; i++){
			array.add(makeJSON(type, BLUMAC, name, image));
		}
		obj.put("list", array);
		return obj.toString();
	}
	
	public JSONObject makeJSON(String type, String BLUMAC, String name, String image){
		JSONObject json = new JSONObject();
		json.put("type", type);
		json.put("BLUMAC", BLUMAC);
		json.put("name", name);
		json.put("imageFile", image);
		return json;
	}
	
	public void addJSONArray(String type, String BLUMAC, String name, String image){
		jsonArray.add(makeJSON(type, BLUMAC, name, image));
	}
	
	public String getJSONArray(){
		obj = new JSONObject();
		obj.put("list", jsonArray);
		return obj.toString();
	}
	
	public void saveFrameImages(DataList data){
		imageList = data;
	}
	
	public void clearFrameImages(){
		imageList = null;
	}
	
	public DataList getImageList(){
		return imageList;
	}
}
