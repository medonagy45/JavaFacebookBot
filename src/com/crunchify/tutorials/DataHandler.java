package com.crunchify.tutorials;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DataHandler {
	static MongoClientURI uri = new MongoClientURI(
			"mongodb://medonagy45:medonagy_2010@cluster0-shard-00-00-t5hjs.mongodb.net:27017,cluster0-shard-00-01-t5hjs.mongodb.net:27017,cluster0-shard-00-02-t5hjs.mongodb.net:27017/Cluster0?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin");

	static MongoClient mongoClient = new MongoClient(uri);

	 @SuppressWarnings("deprecation")
	static DB db = mongoClient.getDB("facebookChatBot");

	  static DBCollection chattingIds = db.getCollection("chattingIds");
	  static DBCollection waitingIds = db.getCollection("waitingIds");
	  static DBCollection joinedBeforeIds = db.getCollection("joinedBeforeIds");
	  

	static Logger logger = Logger.getLogger(MainService.class);
	
	public static boolean isJoinedBefore(String senderId){
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("senderId", senderId);
		DBCursor cursor3 = joinedBeforeIds.find(whereQuery);
		System.out.println(senderId+" has joined before = "+cursor3.hasNext());
		return cursor3.hasNext();
	}
	public static void addNewJoinedBefore(String senderId){
		BasicDBObject data = new BasicDBObject();
		data.append("senderId", senderId);
		joinedBeforeIds.insert(data);
		System.out.println(senderId+" has been added to joined before list");
	}
	public static boolean isInChattingBool(String senderId) {
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("senderId", senderId);
		DBCursor cursor3 = chattingIds.find(whereQuery);
		System.out.println(senderId +" is chatting with somone = "+cursor3.hasNext());
		return cursor3.hasNext();
		
	}
	public static void removeFromChattingBool(String senderId) {
		BasicDBObject whereQuery = new BasicDBObject();
		  whereQuery.put("senderId", senderId);
		  WriteResult cursor3 = chattingIds.remove(whereQuery);
		  System.out.println(senderId+ " was removed from chatting bool.");
	}
	public static void addInChattingBool(String senderId,String recipientId) {
		BasicDBObject data = new BasicDBObject();
		data.append("senderId", senderId);
		data.append("recipientId", recipientId);
		chattingIds.insert(data);
		System.out.println(senderId+ " is chatting to "+recipientId);

	}
	public static String getRecipientId(String senderId) {
		BasicDBObject whereQuery = new BasicDBObject();
		  whereQuery.put("senderId", senderId);
		  DBCursor cursor3 = chattingIds.find(whereQuery);
		  String recipientId="";
		  if (cursor3.hasNext()) {
			  recipientId=(String) cursor3.next().get("recipientId");
		  }
		  System.out.println(recipientId+" is the RecipientId for "+senderId);
		  return recipientId;
	}
	public static boolean isWaitingListEmpty() {
	  DBCursor cursor3 = waitingIds.find();
	  System.out.println("waiting list is empty = "+!cursor3.hasNext());
	  return !cursor3.hasNext();
	}
	public static boolean isInWaitingList(String senderId) {
		BasicDBObject whereQuery = new BasicDBObject();
		  whereQuery.put("senderId", senderId);
		  DBCursor cursor3 = waitingIds.find(whereQuery);
		  System.out.println(senderId+" is In Waiting List "+cursor3.hasNext());
		  return cursor3.hasNext();
	}
	public static void addToWaitingList(String senderId) {
		BasicDBObject data = new BasicDBObject();
		data.append("senderId", senderId);
		waitingIds.insert(data);
		System.out.println(senderId+" is added to Waiting List.");
		
	}
	public static String removeFromWaitingList() {
		DBObject doc = waitingIds.findOne(); //get first document
		waitingIds.remove(doc);
		System.out.println("remove first element From Waiting List "+(String) doc.get("senderId")
				+" and waiting list size is "+waitingIds.find().size());
		return (String) doc.get("senderId");
	}
	
	
	public static void printall(DBCollection collection){
		DBCursor cursor = collection.find();
		  while (cursor.hasNext()) {
			System.out.println(cursor.next());
		  }

	}
	public static void main(String[] args) {
//************** dropping the tables ********************************************************************
//		chattingIds.drop();
//		joinedBeforeIds.drop();
//		waitingIds.drop();
//-------------- print all tables    --------------------------------------------------------------------
		
//		System.out.println("chattingIds");
//		printall(chattingIds);
		System.out.println("joinedBeforeIds");
		printall(joinedBeforeIds);
//		System.out.println("waitingIds");
//		printall(waitingIds);
		
	}
}

