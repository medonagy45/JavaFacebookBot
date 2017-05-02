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
import java.util.Iterator;
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

import org.bson.Document;
import org.json.JSONObject;
//import org.json.JSONString;















import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.jersey.json.impl.JSONHelper;
import com.sun.jersey.json.impl.writer.JsonEncoder;

@Path("/")
public class CrunchifyRESTService {


	private static int messageid = 0;
	@POST
	@Path("/webhook")
	@Consumes(MediaType.APPLICATION_JSON)
	public void crunchifyREST(InputStream incomingData) {
		try {
			RequestParser requestHelper = new RequestParser();
			boolean checkIfShouldReturn = requestHelper
					.checkIfShouldReturn(incomingData);
			if (checkIfShouldReturn) {
				return;
			}
			String senderId = requestHelper.getSenderId();
			RequestHelper.sendInstructions(requestHelper, senderId);
			
			if(RequestHelper.checkAndDisconnect(requestHelper, senderId,Constants.DISCONNECT_ME)){
				return;
			}
			if(RequestHelper.checkAndDisconnect(requestHelper, senderId,Constants.RECONNECT)){
				RequestHelper.handleWaitingAndMatchedCases(requestHelper, senderId);
				return;
			}
			if(RequestHelper.isChatting(senderId)){
				RequestHelper.sendChatMessage(requestHelper, senderId);
				
			}else{
				RequestHelper.handleWaitingAndMatchedCases(requestHelper, senderId);
			}
//			System.out
//				.println("chattingIdsMap.size() " + chattingIdsMap.size());
//			System.out
//				.println("chattingIdsMap " + chattingIdsMap.size());
//			System.out.println("waitingIdsList size  " + waitingIdsList.size());
//			System.out.println("waitingIdsList size  " + waitingIdsList);
//			System.out.println("joinedBefore "+joinedBefore.size());
//			System.out.println("joinedBefore "+joinedBefore );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		MongoClientURI uri = new MongoClientURI(
		"mongodb://medonagy45:medonagy_2010@cluster0-shard-00-00-t5hjs.mongodb.net:27017,cluster0-shard-00-01-t5hjs.mongodb.net:27017,cluster0-shard-00-02-t5hjs.mongodb.net:27017/Cluster0?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin");

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("test");
//		database.getc
//		DB db = mongo.getDB("database name")
		MongoCollection<Document> coll =  database.getCollection("user1");
		
		
		 Document doc = new Document("name", "Andrew12312 Erlichson")//
         .append("company", "10gen");

//		 coll.insertOne(doc); // first insert
		 doc.remove("_id"); // remove the _id key
//		 coll.insertOne(doc); // second insert

		 
		 FindIterable<Document> cursor = coll.find();
         int i = 1;
			for (Iterator it = cursor.iterator(); it
					.hasNext();) {
				
            System.out.println("Inserted Document: "+i); 
            System.out.println(it.next()); 
            i++;
         }
//		table.insertOne(arg0, arg1);
//		System.out.println(coll.find().first());
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
//		result += "joinedBefore "+joinedBefore ;
		return Response.status(200).entity(result).build();
	}

	

}