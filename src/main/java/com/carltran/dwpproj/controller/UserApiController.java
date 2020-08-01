package com.carltran.dwpproj.controller;

import com.carltran.dwpproj.pojo.User;
import com.carltran.dwpproj.pojo.UserList;
import com.carltran.dwpproj.service.BpdtsApiClient;
import com.carltran.dwpproj.service.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
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
  public @ResponseBody List<User> getUsers() {
    UserList allUsers = bpdtsApiClient.getUsers();

    List<User> filteredUsers =
        allUsers.getUsers().stream()
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

    return filteredUsers;
  }
}
