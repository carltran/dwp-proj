package com.carltran.dwpproj.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

class DistanceCalculatorTest {

  private DistanceCalculator distanceCalculator = new DistanceCalculator();

  @Test
  void distanceBetween_whenCoordinatesValid_thenReturnDistance() {
    double longitude1 = -84.3396421;
    double latitude1 = 33.5719791;
    double longitude2 = -0.1278;
    double latitude2 = 51.5074;

    assertThat(distanceCalculator.distanceBetween(longitude1, latitude1, longitude2, latitude2))
        .isCloseTo(4217.83, Percentage.withPercentage(1));
  }

  @Test
  void distanceBetween_whenLessThan60Miles() {
    double longitude1 = 0.1112;
    double latitude1 = 51.9201;
    double longitude2 = -0.1278;
    double latitude2 = 51.5074;

    assertThat(distanceCalculator.distanceBetween(longitude1, latitude1, longitude2, latitude2))
        .isLessThan(60.00);
  }
}
