package com.deploymyapp.build_scheduler.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "github")
@Getter
@Setter
public class GithubProperties {

    private String webhookSecret;
    private String appId;
    private String appKey;
    private String jobUrl;
    private String orgaPat;

}
