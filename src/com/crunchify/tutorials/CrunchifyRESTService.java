package com.crunchify.tutorials;

/**
 * @author Crunchify.com
 *
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;
//import org.json.JSONString;

import com.sun.jersey.json.impl.JSONHelper;
import com.sun.jersey.json.impl.writer.JsonEncoder;

@Path("/")
public class CrunchifyRESTService {

	String token = "EAAGG9TjBpZBYBANajcXNZByydAPiNZApfO9PFudZCaZBmco1ZAghfdKz7CRyQIwKpHCahboBWmk2GU2cbZAV5ZCK9bjcmYkc9bLIbNm7YoGAQcP6LxwxoPpBrhO88q5IYlHbX6DavFxECemUw20bYJAsqF5E3ZARrlKxvYhddbxjAugZDZD";
	static Map<String, String> chattingIdsMap = new HashMap<String, String>();
	static List<String> waitingIdsList = new ArrayList<String>();
	static List<String> joinedBefore = new ArrayList<String>();
	public static int messageid = 0;

	@POST
	@Path("/webhook")
	@Consumes(MediaType.APPLICATION_JSON)
	public void crunchifyREST(InputStream incomingData) {
		try {
			RequestHelper requestHelper = new RequestHelper();
			boolean checkIfShouldReturn = requestHelper
					.checkIfShouldReturn(incomingData);
			if (checkIfShouldReturn) {
				return;
			}
			messageid++;
			String senderId = requestHelper.getSenderId();
			
			if(!joinedBefore.contains(senderId)){
				joinedBefore.add(senderId);
				sendPost(requestHelper.generateResponseMessage(senderId,
					JSONObject.quote("welcome to Friendly Chat bot")));
				sendPost(requestHelper.generateResponseMessage(senderId,
						JSONObject.quote("to end chatting type \"disconnect me\" to reconnect with a new user ")));
				sendPost(requestHelper.generateResponseMessage(senderId,
						JSONObject.quote("to reconnect with a new user type \"reconnect\"")));
				sendPost(requestHelper.generateResponseMessage(senderId,
						JSONObject.quote("once we will found you a match we will notify you")));
			}
			if (chattingIdsMap.containsKey(senderId)) {
				System.out.println("inside chattingid map ");
				System.out.println(requestHelper.getMessageOnly().toLowerCase()
						.equals("\"disconnect me\""));
				if (requestHelper.getMessageOnly().toLowerCase()
						.equals("\"disconnect me\"")) {
					String recipientId = chattingIdsMap.get(senderId);
					chattingIdsMap.remove(senderId);
					chattingIdsMap.remove(recipientId);
					sendPost(requestHelper.generateResponseMessage(senderId,
							JSONObject.quote("you are disconnected, say hi to reconnect")));
					sendPost(requestHelper.generateResponseMessage(recipientId,
							JSONObject.quote("your partner left, say hi to reconnect")));
					return;
				} else if (requestHelper.getMessageOnly().toLowerCase()
						.equals("\"reconnect\"")) {
					String recipientId = chattingIdsMap.get(senderId);
					chattingIdsMap.remove(senderId);
					chattingIdsMap.remove(recipientId);
					sendPost(requestHelper.generateResponseMessage(recipientId,
							JSONObject.quote("your partner left, say hi to reconnect")));
					waitingIdsList.add(senderId);
					return;
				}
				String recipientId = chattingIdsMap.get(senderId);
				sendPost(requestHelper.generateResponse(recipientId));
				return;
				// handle chatting
			}
			
			else if (waitingIdsList.isEmpty()) {
				waitingIdsList.add(senderId);
				sendPost(requestHelper.generateResponseMessage(senderId,
						JSONObject.quote("once we find someone , you will be notified")));
				
			} else if(!waitingIdsList.contains(senderId)){
				String recipientId = waitingIdsList.remove(0);
				chattingIdsMap.put(senderId, recipientId);
				chattingIdsMap.put(recipientId, senderId);
				sendPost(requestHelper.generateResponseMessage(recipientId,
						JSONObject.quote("you are now connected ....")));
				sendPost(requestHelper.generateResponseMessage(senderId,
						JSONObject.quote("you are now connected ....")));
				sendPost(requestHelper.generateResponseMessage(recipientId,
						requestHelper.getMessageOnly()));
			}
			System.out
				.println("chattingIdsMap.size() " + chattingIdsMap.size());
			System.out
				.println("chattingIdsMap " + chattingIdsMap.size());
			System.out.println("waitingIdsList size  " + waitingIdsList.size());
			System.out.println("waitingIdsList size  " + waitingIdsList);
			System.out.println("joinedBefore "+joinedBefore.size());
			System.out.println("joinedBefore "+joinedBefore );
			// chattingIdsMap.put(senderId, senderId);
			// waitingIdsList.add(senderId);
			// String s = requestHelper.generateJsonMessageOnly();
			// sendPost(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@GET
	@Path("/webhook")
	// @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response crunchifyGet(@Context UriInfo uriInfo) {
		String query = uriInfo.getRequestUri().getQuery();
		// String[] requestParams = query.split("&");
		Map<String, List<String>> s = uriInfo.getQueryParameters();
		System.out.println(s.get("hub.verify_token").get(0));
		if (s.get("hub.verify_token").get(0).equals("ThisPassword"))
			return Response.status(200).entity(s.get("hub.challenge").get(0))
					.build();
		return Response.status(200).entity("This is bala7".toString()).build();
	}

	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService() {
		String result = "CrunchifyRESTService Successfully started..\n";
		result += "joinedBefore "+joinedBefore ;
		return Response.status(200).entity(result).build();
	}

	private void sendPost(String body) throws Exception {
		System.out.println("the response id " + messageid + " value : " + body);
		String url = "https://graph.facebook.com/v2.6/me/messages?access_token="
				+ token;

		URL object = new URL(url);

		HttpURLConnection con = (HttpURLConnection) object.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");

		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(body);
		wr.flush();
		wr.close();
		// display what returns the POST request

		int HttpResult = con.getResponseCode();
		if (HttpResult == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println("the response of my send " + messageid
						+ "value :" + line + "\n");
			}
			br.close();
		} else {
			System.out.println("the error message " + messageid + " "
					+ con.getResponseCode());
		}

	}

}