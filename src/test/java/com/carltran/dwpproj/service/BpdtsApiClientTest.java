package com.carltran.dwpproj.service;

import static com.carltran.dwpproj.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.carltran.dwpproj.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

class BpdtsApiClientTest {
  private static final String TEST_URI = "https://bpdts-test-app-v3.herokuapp.com/";

  private MockRestServiceServer mockRestServiceServer;
  private ObjectMapper objectMapper = new ObjectMapper();

  private BpdtsApiClient bpdtsApiClient;

  @BeforeEach
  void setUp() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(TEST_URI));
    mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    bpdtsApiClient = new BpdtsApiClient(restTemplate);
  }

  @Test
  @SneakyThrows
  void getUsers_whenSuccess_thenReceiveAllUsers() {
    List<User> expectedUserList = givenUsers();

    mockRestServiceServer
        .expect(once(), requestTo("https://bpdts-test-app-v3.herokuapp.com/users"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                objectMapper.writeValueAsString(expectedUserList), MediaType.APPLICATION_JSON));

    List<User> userList = bpdtsApiClient.getUsers();

    mockRestServiceServer.verify();
    assertThat(userList).isNotNull();
    assertThat(userList).containsExactlyElementsOf(expectedUserList);
  }

  @Test
  @SneakyThrows
  void getUser_whenSuccess_thenReceiveUser() {
    User expectedUser = givenUser(100);

    mockRestServiceServer
        .expect(once(), requestTo("https://bpdts-test-app-v3.herokuapp.com/user/100"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(objectMapper.writeValueAsString(expectedUser), MediaType.APPLICATION_JSON));

    User user = bpdtsApiClient.getUser(100);
    mockRestServiceServer.verify();
    assertThat(user).isNotNull();
    assertThat(user).isEqualTo(expectedUser);
  }
}
