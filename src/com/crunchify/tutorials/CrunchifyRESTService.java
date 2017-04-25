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

	private boolean checkIfShouldReturn(JSONObject jsonObj) {
		boolean isEcho = jsonObj.getJSONArray("entry").getJSONObject(0)
				.getJSONArray("messaging").getJSONObject(0).has("message") ? jsonObj
				.getJSONArray("entry").getJSONObject(0)
				.getJSONArray("messaging").getJSONObject(0)
				.getJSONObject("message").has("is_echo")
				: false;
		boolean hasDelivery = jsonObj.getJSONArray("entry").getJSONObject(0)
				.getJSONArray("messaging").getJSONObject(0).has("delivery");
		boolean hasRead = jsonObj.getJSONArray("entry").getJSONObject(0)
				.getJSONArray("messaging").getJSONObject(0).has("read");
		return hasDelivery || hasRead || isEcho;
	}

	String token = "EAAGG9TjBpZBYBANajcXNZByydAPiNZApfO9PFudZCaZBmco1ZAghfdKz7CRyQIwKpHCahboBWmk2GU2cbZAV5ZCK9bjcmYkc9bLIbNm7YoGAQcP6LxwxoPpBrhO88q5IYlHbX6DavFxECemUw20bYJAsqF5E3ZARrlKxvYhddbxjAugZDZD";

	@POST
	@Path("/webhook")
	@Consumes(MediaType.APPLICATION_JSON)
	public void crunchifyREST(InputStream incomingData) {

		try {
		StringBuilder crunchifyBuilder = getStringFromInput(incomingData);
//		arg0)
//		String temp=);
//		System.out.println("Temp"+temp);
		JSONObject jsonObj = new JSONObject(crunchifyBuilder.toString());
		System.out.println("json"+jsonObj.toString());
		String senderId = jsonObj.getJSONArray("entry").getJSONObject(0)
				.getJSONArray("messaging").getJSONObject(0)
				.getJSONObject("sender").getString("id");
		boolean checkIfShouldReturn = checkIfShouldReturn(jsonObj);
		if (checkIfShouldReturn) {
			return;
		}
		String message = JSONObject.quote(jsonObj.getJSONArray("entry").getJSONObject(0)
				.getJSONArray("messaging").getJSONObject(0)
				.getJSONObject("message").getString("text"));
		System.out.println(message);
		String s = "{\"recipient\": {\"id\": \"" + senderId
				+ "\"},\"message\": {\"text\": " + message + "}}";
		System.out.println("Data Received: " + s);
			sendPost(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private StringBuilder getStringFromInput(InputStream incomingData) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData,"UTF-8"));
//			BufferedReader in = new BufferedReader(new InputStreamReader(
//					incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				crunchifyBuilder.append(line);
			}
			System.out.println("asdas "+crunchifyBuilder.toString());
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		return crunchifyBuilder;
	}

	@GET
	@Path("/webhook")
	// @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response crunchifyGet(@Context UriInfo uriInfo) {
		String query = uriInfo.getRequestUri().getQuery();
		String[] requestParams = query.split("&");
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
	public Response verifyRESTService(InputStream incomingData) {
		String result = "CrunchifyRESTService Successfully started..";

		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}

	private void sendPost(String body) throws Exception {
//		System.out.println(body);
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

		// display what returns the POST request

		StringBuilder sb = new StringBuilder();
		int HttpResult = con.getResponseCode();
		if (HttpResult == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
//			System.out.println("aaaaa" + sb.toString());
		} else {
			System.out.println(con.getResponseMessage());
		}
	}

}