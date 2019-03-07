# DSS-Coeus-Reporting-Service
Repository with application code for Reporting Service of DSS Coeus Application

## AWS Access Keys
The service requires AWS access keys in order to access the required AWS resources.  The service uses the [AWS credentials default provider chain](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/java-dg-roles.html).  When running on EC2, the keys will be provided by [IAM roles](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/iam-roles-for-amazon-ec2.html) attached to the EC2 instances.

To run the service locally as a standalone service, the environment variables **AWS_ACCESS_KEY_ID** and **AWS_SECRET_ACCESS_KEY** must be set.

## Prerequisite
1. Scala Version 2.12.3
2. SBT Version 1.0.0

## How to run
On the console type **sbt run** to start the service

### Unit Testing
Run **sbt test** to run all the unit tests

##### Scala Style
On the console type **sbt scalastyle**. This produces a list of errors on the console, as well as an XML result file target/scalastyle-result.xml (CheckStyle compatible format).

### Packaging
Run **sbt universal:packageBin** to create a release package