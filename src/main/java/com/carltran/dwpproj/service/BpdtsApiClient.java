package com.carltran.dwpproj.service;

import com.carltran.dwpproj.dto.User;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class BpdtsApiClient {
  private final RestTemplate restTemplate;

  @Autowired
  public BpdtsApiClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<User> getUsers() {
    log.info("Retrieving all users");
    ResponseEntity<List<User>> response =
        restTemplate.exchange(
            UriComponentsBuilder.newInstance().path("/users").toUriString(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<User>>() {});

    return response.getBody();
  }

  public User getUser(Integer id) {
    Assert.notNull(id, "user id must be provided");

    log.info("Retrieving user id: {}", id);
    ResponseEntity<User> responseEntity =
        restTemplate.getForEntity(
            UriComponentsBuilder.newInstance()
                .path("/user")
                .path("/{id}")
                .uriVariables(ImmutableMap.of("id", id))
                .toUriString(),
            User.class);

    return responseEntity.getBody();
  }
}
