package com.crunchify.tutorials;

import java.util.Iterator;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DataHandler {
	public static void main(String[] args) {
		MongoClientURI uri = new MongoClientURI(
		"mongodb://medonagy45:medonagy_2010@cluster0-shard-00-00-t5hjs.mongodb.net:27017,cluster0-shard-00-01-t5hjs.mongodb.net:27017,cluster0-shard-00-02-t5hjs.mongodb.net:27017/Cluster0?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin");

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("test");
//		database.getc
//		DB db = mongo.getDB("database name")
		MongoCollection<Document> coll =  database.getCollection("user1");
		
		
		 Document doc = new Document("name", "123")//
         .append("company", "231");

//		 coll.insertOne(doc); // first insert
		 doc.remove("_id"); // remove the _id key
		 coll.insertOne(doc); // second insert
System.out.println(doc);
		 
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
}
