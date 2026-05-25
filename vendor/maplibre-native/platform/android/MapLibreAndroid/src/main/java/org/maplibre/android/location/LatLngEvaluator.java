package com.mapvina.android.location;

import android.animation.TypeEvaluator;

import androidx.annotation.NonNull;

import com.mapvina.android.geometry.LatLng;

class LatLngEvaluator implements TypeEvaluator<LatLng> {

  private final LatLng latLng = new LatLng();

  @NonNull
  @Override
  public LatLng evaluate(float fraction, @NonNull LatLng startValue, @NonNull LatLng endValue) {
    latLng.setLatitude(startValue.getLatitude()
      + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
    latLng.setLongitude(startValue.getLongitude()
      + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
    return latLng;
  }
}
