package ap.mobile.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Rest {
	private int timeout = 30000;
	private HttpClient httpClient;
	private HttpPost httpPost;
	private HttpGet httpGet;
	private HttpResponse httpResponse;
	
	private String error;
	private String responseString = null;
	
	private ArrayList<NameValuePair> data;
	
	public Rest(){
		this.httpClient = new DefaultHttpClient();
		this.data = new ArrayList<NameValuePair>();
	}
	
	public static Rest getInstance() {
		return new Rest();
	}
	
	public void setTimeout(int timeout){
		this.timeout = timeout;
	}
	
	public Rest post(String url){
		return this.post(url, null);
	}
	
	public Rest post(String url, ArrayList<NameValuePair> data){
		this.httpPost = new HttpPost(url);
		//this.httpPost.setHeader("Cache-Control", "no-cache");
		this.httpClient.getParams().setParameter("http.socket.timeout", this.timeout);
		try {
			this.data = data;
			if(data != null)
				this.httpPost.setEntity(new UrlEncodedFormEntity(this.data));
			//this.httpResponse.setHeader("Cache-Control","no-cache");
			this.httpResponse = this.httpClient.execute(this.httpPost);
		} catch (UnsupportedEncodingException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		}
		return this;
	}

	public Rest get(String url){
		this.httpGet = new HttpGet(url);
		//this.httpGet.setHeader("Cache-Control", "no-cache");
		this.httpClient.getParams().setParameter("http.socket.timeout", this.timeout);
		try {
			//this.httpResponse.setHeader("Cache-Control","no-cache");
			this.httpResponse = this.httpClient.execute(this.httpGet);
			this.getResponseString();
		} catch (UnsupportedEncodingException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		}
		return this;
	}	
	
	public String getError() {
		return this.error;
	}
	
	public String getResponseString(){
		if(this.httpResponse!=null){
			try {
				HttpEntity httpEntity = this.httpResponse.getEntity();
				InputStream is = httpEntity.getContent();
	            BufferedReader reader = new BufferedReader(
	            	new InputStreamReader(is, "utf-8"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            this.responseString = sb.toString();
	            return this.responseString;
	        } catch (Exception e) {
	        	this.error = e.getMessage();
	            Log.e("Rest", "Response string buffer error. " + e.getMessage());
	            e.printStackTrace();
	        }
		} 
		return null;
	}
	
	public JSONObject getResponseJSONObject(){
		String JSONString = this.responseString;
		if(JSONString != null) {
			JSONObject jObj;
			try {
				jObj = new JSONObject(JSONString);
				return jObj;
			} catch (JSONException e) {
				this.error = e.getMessage();
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String getResponseText(){
		return this.responseString;
	}
}
