package com.crelo.pdf.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Data
public class ApplicationConfig {

    @Value("${fireBase.dbBaseUrl}")
    private String dbBaseUrl;

    @Value("${fireBase.collection}")
    private String collectionName;

    @Value("${fireBase.authFileLocation}")
    private String authFileLocation;

}