package com.liondevs.fastfood.authorizationserver.service;


import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.CreateSecretRequest;
import com.amazonaws.services.secretsmanager.model.CreateSecretResult;
import com.liondevs.fastfood.authorizationserver.config.SecretsManagerPullConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AwsSecretsManagerService {
   private final  AWSSecretsManager awsSecretsManager;
   private final SecretsManagerPullConfig secretsManagerPullConfig;
   public void createSecret(String secretName, String secretValue) {
      CreateSecretRequest request = new CreateSecretRequest()
              .withName(secretName)
              .withSecretString(secretValue);

      CreateSecretResult result = awsSecretsManager.createSecret(request);
      String arn = result.getARN();
      log.debug("Created secret with ARN: " + arn);
   }

   public String getSecret(String secretId) {
      try {
         SecretResponse response =  secretsManagerPullConfig.getSecret(secretId, SecretResponse.class);
         return response.SECRET_KEY;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static class SecretResponse{

      public  String SECRET_KEY;
   }

}
