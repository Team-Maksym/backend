package starlight.backend.talent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource("pagination-config.properties")
@ConfigurationProperties(prefix = "pagination-config")
public record PaginationProps(
        int defaultValuePage,
        int defaultValueSize
) {}
