package com.carltran.dwpproj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@SpringBootApplication
@Configuration
public class DwpProjApplication {

  @Value("${bpdts.url}")
  private String bpdtsUrl;

  @Bean
  RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();
    restTemplate.setRequestFactory(requestFactory);
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(bpdtsUrl));
    return restTemplate;
  }

  public static void main(String[] args) {
    SpringApplication.run(DwpProjApplication.class, args);
  }
}
