package com.carltran.dwpproj;

import com.carltran.dwpproj.controller.UserApiController;
import com.carltran.dwpproj.service.BpdtsApiClient;
import com.carltran.dwpproj.service.DistanceCalculator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = {DwpProjApplication.class})
@AutoConfigureMockMvc
class DwpProjApplicationTests {

  @Autowired private BpdtsApiClient bpdtsApiClient;

  @Autowired private DistanceCalculator distanceCalculator;

  @Autowired private UserApiController userApiController;

  @Autowired private MockMvc mockMvc;

  @Test
  void contextLoads() {}

  @Test
  @SneakyThrows
  void getUsers_whenSuccess_thenReturnUsers() {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/users"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data", notNullValue()))
        .andExpect(jsonPath("$.error", nullValue()))
        .andReturn();
  }
}
