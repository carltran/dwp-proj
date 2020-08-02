package com.carltran.dwpproj.controller;

import static com.carltran.dwpproj.TestData.givenMixedUsers;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carltran.dwpproj.dto.User;
import com.carltran.dwpproj.service.BpdtsApiClient;
import com.carltran.dwpproj.service.DistanceCalculator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpServerErrorException;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {

  @MockBean private BpdtsApiClient bpdtsApiClient;

  @Autowired private DistanceCalculator distanceCalculator;

  @Autowired private UserApiController userApiController;

  @Autowired private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void getUsers_whenSuccess_thenReturnUsers() {
    List<User> mixedUserList = givenMixedUsers();
    when(bpdtsApiClient.getUsers()).thenReturn(mixedUserList);
    mixedUserList.get(2).setCity("City 1");
    mixedUserList.get(3).setCity("City 2");
    when(bpdtsApiClient.getUser(1)).thenReturn(mixedUserList.get(2));
    when(bpdtsApiClient.getUser(2)).thenReturn(mixedUserList.get(3));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/users"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data.[0].id", equalTo(1)))
        .andExpect(jsonPath("$.data.[0].email", equalTo("email1@test.com")))
        .andExpect(jsonPath("$.data.[0].first_name", equalTo("first name 1")))
        .andExpect(jsonPath("$.data.[0].last_name", equalTo("last name 1")))
        .andExpect(jsonPath("$.data.[0].ip_address", equalTo("1.1.2.3")))
        .andExpect(jsonPath("$.data.[0].longitude", equalTo(-0.1178)))
        .andExpect(jsonPath("$.data.[0].latitude", equalTo(51.9201)))
        .andExpect(jsonPath("$.data.[0].city", equalTo("City 1")))
        .andExpect(jsonPath("$.data.[1].id", equalTo(2)))
        .andExpect(jsonPath("$.data.[1].email", equalTo("email2@test.com")))
        .andExpect(jsonPath("$.data.[1].first_name", equalTo("first name 2")))
        .andExpect(jsonPath("$.data.[1].last_name", equalTo("last name 2")))
        .andExpect(jsonPath("$.data.[1].ip_address", equalTo("2.1.2.3")))
        .andExpect(jsonPath("$.data.[1].longitude", equalTo(0.1112)))
        .andExpect(jsonPath("$.data.[1].latitude", equalTo(51.9201)))
        .andExpect(jsonPath("$.data.[1].city", equalTo("City 2")))
        .andExpect(jsonPath("$.data.[2]").doesNotExist()) // no more than 2 users
        .andExpect(jsonPath("$.error", nullValue()));

    verify(bpdtsApiClient, times(1)).getUsers();
    verify(bpdtsApiClient, times(2)).getUser(any());
  }

  @Test
  @SneakyThrows
  void getUsers_whenBpdtsServerError_thenReturn500() {
    when(bpdtsApiClient.getUsers())
        .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error"));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/users"))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.error.message", equalTo("500 Server error")));
  }

  @Test
  @SneakyThrows
  void getUsers_whenBpdtsServiceUnavailable_thenReturn500() {
    when(bpdtsApiClient.getUsers())
        .thenThrow(
            new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable"));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/users"))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.error.message", equalTo("503 Service unavailable")));
  }
}
