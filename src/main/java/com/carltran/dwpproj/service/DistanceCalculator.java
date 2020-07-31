package com.carltran.dwpproj.service;

import org.springframework.stereotype.Service;

@Service
public class DistanceCalculator {
  /**
   * Using Haversine formula https://en.wikipedia.org/wiki/Haversine_formula
   *
   * <p>This method calculate flat distance between 2 coordinates in miles
   */
  public final double distanceBetween(
      double longitude1, double latitude1, double longitude2, double latitude2) {
    final double earthRadius = 3963;
    double latitudeDeltaInRad = deg2rad(latitude2 - latitude1);
    double longitudeDeltaInRad = deg2rad(longitude2 - longitude1);
    double a =
        Math.sin(latitudeDeltaInRad / 2) * Math.sin(latitudeDeltaInRad / 2)
            + Math.cos(deg2rad(latitude1))
                * Math.cos(deg2rad(latitude2))
                * Math.sin(longitudeDeltaInRad / 2)
                * Math.sin(longitudeDeltaInRad / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = earthRadius * c;

    return distance;
  }

  private static final double deg2rad(double degree) {
    return degree * (Math.PI / 180);
  }
}
