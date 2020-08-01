package com.carltran.dwpproj.controller;

import com.carltran.dwpproj.dto.Error;
import com.carltran.dwpproj.dto.User;
import com.carltran.dwpproj.dto.UsersResponse;
import com.carltran.dwpproj.service.BpdtsApiClient;
import com.carltran.dwpproj.service.DistanceCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserApiController {

  private static final Double LONDON_LATITUDE = 51.5074;
  private static final Double LONDON_LONGITUDE = -0.1278;

  private final DistanceCalculator distanceCalculator;
  private final BpdtsApiClient bpdtsApiClient;

  @Autowired
  public UserApiController(DistanceCalculator distanceCalculator, BpdtsApiClient bpdtsApiClient) {
    this.distanceCalculator = distanceCalculator;
    this.bpdtsApiClient = bpdtsApiClient;
  }

  /**
   * Return a list of users in London or within 60 miles from it
   *
   * @return list of users
   */
  @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UsersResponse> getUsers() {
    try {
      log.info("Calling API to get all users");
      List<User> allUsers = bpdtsApiClient.getUsers();

      log.info("Filtering users close to target location and retrieving full user details");
      List<User> filteredUsers =
          allUsers.stream()
              .filter(
                  user ->
                      distanceCalculator.distanceBetween(
                              LONDON_LONGITUDE,
                              LONDON_LATITUDE,
                              user.getLongitude(),
                              user.getLatitude())
                          <= 60)
              .map(user -> bpdtsApiClient.getUser(user.getId()))
              .collect(Collectors.toList());

      return new ResponseEntity<>(
          UsersResponse.builder().data(filteredUsers).build(), HttpStatus.OK);
    } catch (RestClientException ex) {
      log.error("Encountering error: ", ex);
      return new ResponseEntity<>(
          UsersResponse.builder().error(Error.builder().message(ex.getMessage()).build()).build(),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
