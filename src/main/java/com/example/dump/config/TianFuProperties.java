package com.example.dump.config;

import com.example.dump.utils.QueryTianFu;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tianfu")
public class TianFuProperties {

    private String secret;
    private String key;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @PostConstruct
    public void init() {
        QueryTianFu.setSecret(secret);
        QueryTianFu.setKey(key);
    }
}