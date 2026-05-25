package com.mapvina.android.location;

import android.animation.TypeEvaluator;

import androidx.annotation.NonNull;

import com.mapvina.android.geometry.LatLng;

class MapVinaLatLngAnimator extends MapVinaAnimator<LatLng> {

  MapVinaLatLngAnimator(@NonNull LatLng[] values, @NonNull AnimationsValueChangeListener updateListener,
                         int maxAnimationFps) {
    super(values, updateListener, maxAnimationFps);
  }

  @NonNull
  @Override
  TypeEvaluator provideEvaluator() {
    return new LatLngEvaluator();
  }
}
