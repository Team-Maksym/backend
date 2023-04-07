package starlight.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@Slf4j
@ConfigurationProperties(prefix = "s3")
public record S3Props (
        String bucket,
        String accessKey,
        String secretKey,
        String region
){
    @PostConstruct
    void logLoaded() {
        log.info("props = {}", this);
    }
}
