# FoodSurvey

Food Survey Application with Java Spring Boot and AWS Services.
- - -
# Project Overview
**Application Components**

**Backend**
- Java Spring Boot
- Maven
- JDK 17
- Dockerfile
- AWS Lambda
- AWS Simple Notification Service (SNS)
- AWS S3
- appspec.yml
- buildspec.yml
- X-Ray SDK

**Frontend**

The frontend of the Food Survey Application is responsible for presenting food images retrieved
from an AWS S3 bucket to users. Users rate these images on a scale of 1 to 10, contributing to the
overall user experience.
- - -

IMAGE

**X-Ray Tracing**

X-Ray tracing is enabled for the entire controller and specific methods in the Service class such ass
getting all ImagesFromS3Bucket, saveRatings invokeLambda.

# Application Workflow

- Users interact with the frontend to view food images and rate them.
- The backend retrieves images from an AWS S3 bucket.
- Users rate the images 1-10.
- If an image receives a rating of 1, 2, or 3, a Lambda function is invoked.
- The Lambda function sends an SNS notification.

# AWS Services Used During Development
- AWS CodeCommit
- AWS CodeBuild
- AWS CodeDeploy
- AWS CodePipeline
- Amazon EC2
- Amazon CloudWatch
- Amazon S3



# IAM role and policy
**CodebuildServiceRole**


- codeBuildBatchPolicy
- codeBuildSerivceRolePolicy

**CodebuildServiceplociy**

        {
        "Version": "2012-10-17",
        "Statement": [
        {
        "Sid": "VisualEditor0",
        "Effect": "Allow",
        "Action": [
        "codecommit:GitPull",
        "s3:GetBucketAcl",
        "logs:CreateLogGroup",
        "logs:PutLogEvents",
        "s3:PutObject",
        "s3:GetObject",
        "logs:CreateLogStream",
        "ecr:BatchGetImage",
        "s3:GetBucketLocation",
        "s3:GetObjectVersion"
        ],
        "Resource": "*"
        }
        ]
        }

**codeBuildBatchPolicy**

    {
    "Version": "2012-10-17",
    "Statement": [
    {
    "Effect": "Allow",
    "Resource": [
    "arn:aws:codebuild:Region:Acount-ID:project/sample"
    ],
    "Action": [
    "codebuild:StartBuild",
    "codebuild:StopBuild",
    "codebuild:RetryBuild"
    ]
    }
    ]
    }

**ec2role**
- AWSXrayFullAccess
- AWSXRayDaemonWriteAccess
- AWSCodeDeployFullAccess
# Diagram/Design
![FoodSurvey.drawio4.drawio.png](FoodSurvey.drawio4.drawio.png)
	



# FoodSurvey
