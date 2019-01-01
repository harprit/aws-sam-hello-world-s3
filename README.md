sam local invoke --event SampleEvent.json

Pleas note that an existing bucket cannot be used as an event source.
https://github.com/serverless/serverless/issues/3257

sam package --template-file template.yaml --output-template-file packaged.yaml --s3-bucket <bucketname>

sam deploy --template-file packaged.yaml --stack-name <stackname>  --capabilities CAPABILITY_IAM 

aws s3 cp hello-world.txt s3://<bucketname>

