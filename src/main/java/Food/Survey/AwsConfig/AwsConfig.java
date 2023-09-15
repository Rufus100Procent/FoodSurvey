package Food.Survey.AwsConfig;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import org.springframework.stereotype.Component;

@Component
public class AwsConfig {
    private static final String accessKey = "acceskey";
    private static final String secretKey = "secretkey";
    private static final Regions region = Regions.EU_NORTH_1; // Replace with your desired region

    public static AWSStaticCredentialsProvider getCredentialsProvider() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return new AWSStaticCredentialsProvider(credentials);
    }

    public static Regions getRegion() {
        return region;
    }
}