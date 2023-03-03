package com.liondevs.fastfood.authorizationserver.config;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liondevs.fastfood.authorizationserver.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SecretsManagerPullConfig {


    public <T> T getSecret(String secretName, Class<T> valueType) throws Exception {
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.defaultClient();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);

        GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);

        String secretString = getSecretValueResult.getSecretString();
        log.info(secretString);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(secretString, valueType);
    }
}
