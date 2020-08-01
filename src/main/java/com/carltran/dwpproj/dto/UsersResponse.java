package com.carltran.dwpproj.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UsersResponse {
  private List<User> data;

  private Error error;
}
