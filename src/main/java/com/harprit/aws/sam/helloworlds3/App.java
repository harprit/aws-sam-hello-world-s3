package com.harprit.aws.sam.helloworlds3;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity;

public class App {

	public void handleRequest(S3Event s3Event) {

		S3Entity s3Entity = s3Event.getRecords().get(0).getS3();

		String key = s3Entity.getObject().getKey();

		System.out.format("There's a new file named %s in %s bucket", key, s3Entity.getBucket().getName());
	}
}
