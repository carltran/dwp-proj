package com.carltran.dwpproj.controller;

import com.carltran.dwpproj.pojo.UserList;
import com.carltran.dwpproj.service.BpdtsApiClient;
import com.carltran.dwpproj.service.DistanceCalculator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.carltran.dwpproj.TestData.givenMixedUserList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    UserList mixedUserList = givenMixedUserList();
    when(bpdtsApiClient.getUsers()).thenReturn(mixedUserList);
    mixedUserList.getUsers().get(2).setCity("City 1");
    mixedUserList.getUsers().get(3).setCity("City 2");
    when(bpdtsApiClient.getUser(1)).thenReturn(mixedUserList.getUsers().get(2));
    when(bpdtsApiClient.getUser(2)).thenReturn(mixedUserList.getUsers().get(3));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/users"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id", equalTo(1)))
        .andExpect(jsonPath("$[0].city", notNullValue()))
        .andExpect(jsonPath("$[1].id", equalTo(2)))
        .andExpect(jsonPath("$[1].city", notNullValue()))
        .andExpect(jsonPath("$[2].id").doesNotExist())
        .andReturn();

    verify(bpdtsApiClient, times(1)).getUsers();
    verify(bpdtsApiClient, times(2)).getUser(any());
  }
}
