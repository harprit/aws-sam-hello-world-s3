package com.harprit.helloworlds3;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class App {

    private static final AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard().build();

    private static final AmazonDynamoDB dynamoDbClient = AmazonDynamoDBClientBuilder.standard().build();
    private static final DynamoDB dynamoDB = new DynamoDB(dynamoDbClient);
    private static final Table table = dynamoDB.getTable(System.getenv("TABLE_NAME"));

    public void handleRequest(S3Event s3Event) {
        S3Entity s3Entity = s3Event.getRecords().get(0).getS3();
        String bucketName = s3Entity.getBucket().getName();
        String fileName = s3Entity.getObject().getKey();
        System.out.format("There's a new file named %s in %s bucket\n", fileName, bucketName);

        String fileContent = loadS3FileContent(bucketName, fileName);

        putFileContentInDB(fileName, fileContent);
    }

    private String loadS3FileContent(String bucketName, String fileName) {
        S3Object s3Object = amazonS3Client.getObject(bucketName, fileName);
        InputStreamReader streamReader = new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        StringBuilder stringBuilder = new StringBuilder();
        reader.lines().forEach(line -> stringBuilder.append(line).append(" "));
        return stringBuilder.toString();
    }

    private void putFileContentInDB(String fileName, String fileContent) {
        System.out.format("Inserting content of file %s into table: %s\n", fileName, table.getTableName());
        Item item = new Item()
                .withPrimaryKey("fileName", fileName)
                .withLong("timestamp", new Date().getTime())
                .withString("fileContent", fileContent);
        table.putItem(item);
    }
}
