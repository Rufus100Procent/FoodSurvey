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

![Screenshot from 2023-09-13 12-57-03](https://github.com/Rufus100Procent/FoodSurvey/assets/66412126/c000e7e6-9535-481e-b834-280b4075df66)


**X-Ray Tracing**

X-Ray tracing is enabled for the entire controller and specific methods in the Service class such ass
getting all ImagesFromS3Bucket, saveRatings invokeLambda.

![Screenshot from 2023-09-04 17-03-56](https://github.com/Rufus100Procent/FoodSurvey/assets/66412126/ef6d2c0f-bab9-41e9-82f9-7cbfada6192c)


# Application Workflow

- Users interact with the frontend to view food images and rate them.
- The backend retrieves images from an AWS S3 bucket.
- Users rate the images 1-10.
- If an image receives a rating of 1, 2, or 3, a Lambda function is invoked.
- SNS notification will be sent out.

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

![FoodSurvey drawio4 drawio](https://github.com/Rufus100Procent/FoodSurvey/assets/66412126/1843a505-4e5d-4888-bcfd-46c8d2ec844b)
