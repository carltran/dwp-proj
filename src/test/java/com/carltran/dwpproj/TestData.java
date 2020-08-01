package com.carltran.dwpproj;

import com.carltran.dwpproj.pojo.User;
import com.carltran.dwpproj.pojo.UserList;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TestData {
  public static User givenUser(Integer id) {
    return User.builder()
        .id(id)
        .firstName("first name " + id)
        .lastName("last name 2" + id)
        .email("email" + id + "@test.com")
        .latitude(Math.random())
        .longitude(Math.random())
        .ipAddress(id + ".1.2.3")
        .build();
  }

  public static List<User> givenUsers() {
    return ImmutableList.of(givenUser(1), givenUser(2));
  }

  public static UserList givenUserList() {
    UserList userList = new UserList();
    userList.setUsers(givenUsers());
    return userList;
  }

  public static UserList givenMixedUserList() {
    UserList userList = new UserList();
    Iterable<User> combinedIterables =
        Iterables.unmodifiableIterable(
            Iterables.concat(givenUsersFarFromLondon(), givenUsersNearLondon()));

    userList.setUsers(Lists.newArrayList(combinedIterables));

    return userList;
  }

  public static List<User> givenUsersNearLondon() {
    User user1 = givenUser(1);
    user1.setLatitude(51.9201);
    user1.setLongitude(-0.1178);

    User user2 = givenUser(2);
    user2.setLatitude(51.9201);
    user2.setLongitude(0.1112);

    return ImmutableList.of(user1, user2);
  }

  public static List<User> givenUsersFarFromLondon() {
    User user1 = givenUser(100);
    user1.setLatitude(52.5074);
    user1.setLongitude(23.1478);

    User user2 = givenUser(101);
    user2.setLatitude(12.2344);
    user2.setLongitude(-12.1112);

    return ImmutableList.of(user1, user2);
  }
}
