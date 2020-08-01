package com.carltran.dwpproj.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Error {
  private String message;
}
