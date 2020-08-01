package com.carltran.dwpproj.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UsersResponse {
  private List<User> data;

  private Error error;
}
