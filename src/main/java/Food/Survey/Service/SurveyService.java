package Food.Survey.Service;

import Food.Survey.AwsConfig.AwsConfig;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@XRayEnabled
public class SurveyService {
    private final List<String> imageUrls = new ArrayList<>();
    private final Map<String, List<Integer>> ratings = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(SurveyService.class);

    private final AmazonS3 s3Client;

    public SurveyService(AwsConfig awsConfig) {
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(AwsConfig.getCredentialsProvider())
                .withRegion(AwsConfig.getRegion())
                .build();
    }

    @XRayEnabled
    public List<String> getAllImagesFromS3Bucket(String bucketName) {
        logger.info("Getting all image URLs from S3 bucket: {}", bucketName);
        Subsegment s3Subsegment = AWSXRay.beginSubsegment("getAllImagesFromS3Bucket");
        try {
            ListObjectsV2Request listRequest = new ListObjectsV2Request()
                    .withBucketName(bucketName);

            ListObjectsV2Result objects = s3Client.listObjectsV2(listRequest);
            List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();

            if (!objectSummaries.isEmpty()) {
                for (S3ObjectSummary objectSummary : objectSummaries) {
                    String imageUrl = objectSummary.getKey();
                    imageUrls.add(imageUrl);
                    ratings.put(imageUrl, new ArrayList<>());
                }
            }

            logger.info("Image URLs retrieved: {}", imageUrls);
            return imageUrls;
        } catch (Exception e) {
            s3Subsegment.addException(e);
            throw e;
        } finally {
            AWSXRay.endSubsegment();
        }
    }
    @XRayEnabled
    public void saveRating(String imageUrl, int rating) {
        List<Integer> imageRatings = ratings.get(imageUrl);
        Subsegment subsegment = AWSXRay.beginSubsegment("saveRating");
        try {
            if (imageRatings != null) {
                imageRatings.add(rating);
                logger.info("Rating saved for image {}: {}", imageUrl, rating);

                if (rating < 3) {
                    logger.info("Image {} has been rated less than 3", imageUrl);
                    invokeLambda(imageUrl, rating);
                }
            }
        } catch (Exception e) {
            subsegment.addException(e);
            throw e;
        } finally {
            AWSXRay.endSubsegment();
        }
    }

    @XRayEnabled
    public void invokeLambda(String imageName, int rating) {
        AWSLambda lambda = AWSLambdaClientBuilder.standard()
                .withCredentials(AwsConfig.getCredentialsProvider())
                .withRegion(AwsConfig.getRegion())
                .build();

        InvokeRequest request = new InvokeRequest()
                .withFunctionName("LambdafunctionName");
        InvokeResult result = lambda.invoke(request);

        Subsegment lambdaSubsegment = AWSXRay.beginSubsegment("LambdaInvocation");
        try {

            ByteBuffer byteBuffer = result.getPayload();
            String responseString = StandardCharsets.UTF_8.decode(byteBuffer).toString();
            logger.info("Lambda response: {}", responseString);

            AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                    .withRegion(AwsConfig.getRegion())
                    .withCredentials(AwsConfig.getCredentialsProvider())
                    .build();

            String snsMessage = "Lambda function triggered due to low rating for image: '\n' " + imageName
                    + ": has been rated " + rating;
            PublishRequest publishRequest = new PublishRequest()
                    .withTopicArn("arn:aws:sns:")
                    .withMessage(snsMessage);

            PublishResult publishResult = snsClient.publish(publishRequest);
            logger.info("Message published to SNS: {}", publishResult.getMessageId());
        } catch (Exception e) {
            lambdaSubsegment.addException(e);
            throw e;
        } finally {
            AWSXRay.endSubsegment();
        }
    }



    public Map<String, List<Integer>> getAllRatings() {
        return ratings;
    }
}