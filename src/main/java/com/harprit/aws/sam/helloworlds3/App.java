package com.harprit.aws.sam.helloworlds3;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity;

public class App {

	private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	private static DynamoDB dynamoDB = new DynamoDB(client);
	private static Table table = dynamoDB.getTable(System.getenv("TABLE_NAME"));

	public void handleRequest(S3Event s3Event) {

		getFileNameFromS3(s3Event);
		putFileNameInDB();
	}

	private void getFileNameFromS3(S3Event s3Event) {

		S3Entity s3Entity = s3Event.getRecords().get(0).getS3();
		String key = s3Entity.getObject().getKey();
		System.out.format("There's a new file named %s in %s bucket", key, s3Entity.getBucket().getName());
	}

	private void putFileNameInDB() {
		
		System.out.println("Inserting into tbale name: " + table.getTableName());
		
		Item item = new Item().withPrimaryKey("id", "123").withString("Title", "Bicycle 123");
		PutItemOutcome putItemOutcome = table.putItem(item);
		
		System.out.println(putItemOutcome.getPutItemResult().toString());
	}
}
