package com.carltran.dwpproj.service;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DistanceCalculatorTest {

  private DistanceCalculator distanceCalculator = new DistanceCalculator();

  @Test
  void distanceBetween_whenCoordinatesValid_thenReturnDistance() {
    double longitude1 = -84.3396421;
    double longitude2 = -0.1278;
    double latitude1 = 33.5719791;
    double latitude2 = 51.5074;

    assertThat(distanceCalculator.distanceBetween(longitude1, latitude1, longitude2, latitude2))
        .isCloseTo(4217.83, Percentage.withPercentage(1));
  }
}
