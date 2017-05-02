package com.crunchify.tutorials;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

import org.json.JSONObject;

public class RequestParser {
	JSONObject messaging;
	boolean isAttachment=false;
	public boolean checkIfShouldReturn(InputStream incomingData) throws ParseException {
		JSONObject jsonObj=new JSONObject(getStringFromInput(incomingData));
		messaging=getMessagingJsonObject(jsonObj);
		
		boolean isEcho = messaging.has("message") ? messaging
				.getJSONObject("message").has("is_echo")
				: false;
		if(isEcho)
			return true;
		boolean hasDelivery = messaging.has("delivery");
		if(hasDelivery)
			return true;
		boolean hasRead = messaging.has("read");
		return hasRead ;
	}

	public String getMessageOnly() {

		if(messaging.getJSONObject("message").has("text")){
		if(message==null)
			message =JSONObject.quote(messaging.getJSONObject("message").getString("text"));
		return message;
		}
		return "";
	}
	public String getِِِAttachment() {
		if(message==null)
			message =JSONObject.quote(messaging.getJSONObject("message").getJSONArray("attachments")
					.getJSONObject(0).getJSONObject("payload").getString("url"));
		return message;
	}
	
	private String senderId;
	private String message;
	public String getSenderId() {		
		if(senderId==null)
			senderId=messaging.getJSONObject("sender").getString("id");
//		System.out.println("this is the recieved message "+CrunchifyRESTService.messageid+" " +messaging);
		return senderId;
	}

	public String generateResponse(String id) {
		System.out.println(messaging.getJSONObject("message").has("text"));
		if(messaging.getJSONObject("message").has("text")){
			String message = getMessageOnly();
			return generateResponseMessage(id,message);
		}
		String attachment= getِِِAttachment();
		return generateResponseMessage(id,attachment);
	}

	public String generateResponseMessage( String id,String message) {
		return "{\"recipient\": {\"id\": \"" + id
				+ "\"},\"message\": {\"text\": " + message + "}}";
	}
	
	public String generateResponseAttachment( String id,String attachment) {
		return "{\"recipient\": {\"id\": \"" + id
				+ "\"},\"message\": {\"attachment\":{\"payload\":{\"url\":"+attachment+"},\"type\":\"image\"}}";
	}

	private JSONObject getMessagingJsonObject(JSONObject jsonObjParent) {
		return jsonObjParent.getJSONArray("entry").getJSONObject(0)
				.getJSONArray("messaging").getJSONObject(0);
	}
	private String getStringFromInput(InputStream incomingData) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData,"UTF-8"));
//			BufferedReader in = new BufferedReader(new InputStreamReader(
//					incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
//				System.out.println(line);
				crunchifyBuilder.append(line);
			}
//			System.out.println("asdas "+crunchifyBuilder.toString());
		} catch (Exception e) {
			System.out.println("Error Parsing: - "+e.getMessage());
		}
		return crunchifyBuilder.toString();
	}
	
}
