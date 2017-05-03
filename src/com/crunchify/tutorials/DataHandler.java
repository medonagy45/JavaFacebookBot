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
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DataHandler {
	static MongoClientURI uri = new MongoClientURI(
			"mongodb://medonagy45:medonagy_2010@cluster0-shard-00-00-t5hjs.mongodb.net:27017,cluster0-shard-00-01-t5hjs.mongodb.net:27017,cluster0-shard-00-02-t5hjs.mongodb.net:27017/Cluster0?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin");

	static MongoClient mongoClient = new MongoClient(uri);

	 static DB db = mongoClient.getDB("facebookChatBot");

	  DBCollection chattingIds = db.getCollection("chattingIds");
	  DBCollection waitingIds = db.getCollection("waitingIds");
	  static DBCollection joinedBeforeIds = db.getCollection("joinedBeforeIds");
	  
	private static Map<String, String> chattingIdsMap = new HashMap<String, String>();
	private static List<String> waitingIdsList = new ArrayList<String>();
	private static List<String> joinedBefore = new ArrayList<String>();

	static Logger logger = Logger.getLogger(MainService.class);
	
	public static boolean isJoinedBefore(String senderId){
		System.out.println("hi*//********************************************************************");
		logger.info("hello");
		BasicDBObject whereQuery = new BasicDBObject();
		  whereQuery.put("senderId", senderId);
		  DBCursor cursor3 = joinedBeforeIds.find(whereQuery);
		  return cursor3.hasNext();
//		return joinedBefore.contains(senderId);
	}
	public static void addNewJoinedBefore(String senderId){
		BasicDBObject data = new BasicDBObject();
		data.append("senderId", senderId);
		joinedBeforeIds.insert(data);
	}
	public static boolean isInChattingBool(String senderId) {
		return chattingIdsMap.containsKey(senderId);
	}
	public static void removeFromChattingBool(String senderId) {
		 chattingIdsMap.remove(senderId);
	}
	public static void addInChattingBool(String senderId,String recipientId) {
		chattingIdsMap.put(senderId,recipientId);
	}
	public static String getRecipientId(String senderId) {
		return chattingIdsMap.get(senderId);
	}
	public static boolean isWaitingListEmpty() {
		return waitingIdsList.isEmpty();
	}
	public static boolean isInWaitingList(String senderId) {
		return waitingIdsList.contains(senderId);
	}
	public static void addToWaitingList(String senderId) {
		waitingIdsList.add(senderId);
	}
	public static String removeFromWaitingList() {
		return waitingIdsList.remove(0);
	}
	
	
	public static void insertDummyDocuments(DBCollection collection) {

		List<DBObject> list = new ArrayList<DBObject>();

		Calendar cal = Calendar.getInstance();

		for (int i = 1; i <= 5; i++) {

			BasicDBObject data = new BasicDBObject();
			data.append("number", i);
			data.append("name", "mkyong-" + i);
			// data.append("date", cal.getTime());

			// +1 day
//			cal.add(Calendar.DATE, 1);

			list.add(data);

		}

		collection.insert(list);

	}

	public static void main(String[] args) {

	try {


	  // get a single collection
	  DBCollection collection = db.getCollection("dummyColl");

	  insertDummyDocuments(collection);

	  System.out.println("1. Find first matched document");
	  DBObject dbObject = collection.findOne();
	  System.out.println(dbObject);

	  System.out.println("\n1. Find all matched documents");
	  DBCursor cursor = collection.find();
	  while (cursor.hasNext()) {
		System.out.println(cursor.next());
	  }

	  System.out.println("\n1. Get 'name' field only");
	  BasicDBObject allQuery = new BasicDBObject();
	  BasicDBObject fields = new BasicDBObject();
	  fields.put("name", 1);

	  DBCursor cursor2 = collection.find(allQuery, fields);
	  while (cursor2.hasNext()) {
		System.out.println(cursor2.next());
	  }

	  System.out.println("\n2. Find where number = 5");
	  BasicDBObject whereQuery = new BasicDBObject();
	  whereQuery.put("number", 5);
	  DBCursor cursor3 = collection.find(whereQuery);
	  while (cursor3.hasNext()) {
		System.out.println(cursor3.next());
	  }

	  System.out.println("\n2. Find where number in 2,4 and 5");
	  BasicDBObject inQuery = new BasicDBObject();
	  List<Integer> list = new ArrayList<Integer>();
	  list.add(2);
	  list.add(4);
	  list.add(5);
	  inQuery.put("number", new BasicDBObject("$in", list));
	  DBCursor cursor4 = collection.find(inQuery);
	  while (cursor4.hasNext()) {
		System.out.println(cursor4.next());
	  }

	  System.out.println("\n2. Find where 5 > number > 2");
	  BasicDBObject gtQuery = new BasicDBObject();
	  gtQuery.put("number", new BasicDBObject("$gt", 2).append("$lt", 5));
	  DBCursor cursor5 = collection.find(gtQuery);
	  while (cursor5.hasNext()) {
		System.out.println(cursor5.next());
	  }

	  System.out.println("\n2. Find where number != 4");
	  BasicDBObject neQuery = new BasicDBObject();
	  neQuery.put("number", new BasicDBObject("$ne", 4));
	  DBCursor cursor6 = collection.find(neQuery);
	  while (cursor6.hasNext()) {
		System.out.println(cursor6.next());
	  }

	  System.out.println("\n3. Find when number = 2 and name = 'mkyong-2' example");
	  BasicDBObject andQuery = new BasicDBObject();

	  List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
	  obj.add(new BasicDBObject("number", 2));
	  obj.add(new BasicDBObject("name", "mkyong-2"));
	  andQuery.put("$and", obj);

	  System.out.println(andQuery.toString());

	  DBCursor cursor7 = collection.find(andQuery);
	  while (cursor7.hasNext()) {
		System.out.println(cursor7.next());
	  }

	  System.out.println("\n4. Find where name = 'Mky.*-[1-3]', case sensitive example");
	  BasicDBObject regexQuery = new BasicDBObject();
	  regexQuery.put("name",
		new BasicDBObject("$regex", "Mky.*-[1-3]")
                    .append("$options", "i"));

	  System.out.println(regexQuery.toString());

	  DBCursor cursor8 = collection.find(regexQuery);
	  while (cursor8.hasNext()) {
		System.out.println(cursor8.next());
	  }

	  collection.drop();

	  System.out.println("Done");

	 } catch (MongoException e) {
		e.printStackTrace();
	 }

	}
}

