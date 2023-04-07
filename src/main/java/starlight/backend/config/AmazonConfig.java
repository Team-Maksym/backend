package starlight.backend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@AllArgsConstructor
public class AmazonConfig {
    S3Props s3Props;

/*    @Bean
    public AmazonS3 s3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(
                s3Props.accessKey(),
                s3Props.secretKey()
        );
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(s3Props.region())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }*/

    public AWSCredentials credentials() {
        AWSCredentials credentials = new BasicAWSCredentials(
                s3Props.accessKey(),
                s3Props.secretKey()
        );
        return credentials;
    }

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
        return s3client;
    }
}