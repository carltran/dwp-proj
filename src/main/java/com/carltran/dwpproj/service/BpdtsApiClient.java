package com.carltran.dwpproj.service;

import com.carltran.dwpproj.pojo.User;
import com.carltran.dwpproj.pojo.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.google.common.collect.ImmutableMap;

@Service
public class BpdtsApiClient {
  private final RestTemplate restTemplate;

  @Autowired
  public BpdtsApiClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public UserList getUsers() {
    return restTemplate.getForObject(
        UriComponentsBuilder.newInstance().path("/users").toUriString(), UserList.class);
  }

  public User getUser(Integer id) {
    Assert.notNull(id, "user id must be provided");

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
