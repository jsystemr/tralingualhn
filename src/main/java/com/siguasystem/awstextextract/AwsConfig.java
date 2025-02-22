package com.siguasystem.awstextextract;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.translate.TranslateClient;

@Configuration
public class AwsConfig {
	

	@Bean
    public TextractClient textractClient() {
		/*Variables AWS se reemplazaraon por variables de session*/
		String textractAccessKey=System.getenv("textractAccessKey");
		String textractSecretKey=System.getenv("textractSecretKey");
	     String region="us-east-2";
        return TextractClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(textractAccessKey, textractSecretKey)
                ))
                .build();
    }

    @Bean
    public TranslateClient translateClient() {
    	/*Variables AWS se reemplazaraon por variables de session*/
		String translateAccessKey=System.getenv("translateAccessKey");
		String translateSecretKey=System.getenv("translateSecretKey");
	     String region="us-east-2";
        return TranslateClient.builder()
        		.region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(translateAccessKey, translateSecretKey)
                ))
                .build();
    }
}
