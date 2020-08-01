package com.carltran.dwpproj.controller;

import static com.carltran.dwpproj.TestData.givenMixedUsers;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carltran.dwpproj.dto.User;
import com.carltran.dwpproj.service.BpdtsApiClient;
import com.carltran.dwpproj.service.DistanceCalculator;
import java.util.List;
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
        .andExpect(jsonPath("$.data.[0].city", notNullValue()))
        .andExpect(jsonPath("$.data.[1].id", equalTo(2)))
        .andExpect(jsonPath("$.data.[1].city", notNullValue()))
        .andExpect(jsonPath("$.data.[2].id").doesNotExist())
        .andExpect(jsonPath("$.error", nullValue()))
        .andReturn();

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
