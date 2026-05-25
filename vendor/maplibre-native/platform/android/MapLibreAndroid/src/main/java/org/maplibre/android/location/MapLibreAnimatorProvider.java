package com.mapvina.android.location;

import android.animation.ValueAnimator;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;

import com.mapvina.android.maps.MapVinaMap;
import com.mapvina.android.geometry.LatLng;

final class MapVinaAnimatorProvider {

  private static MapVinaAnimatorProvider INSTANCE;

  private MapVinaAnimatorProvider() {
    // private constructor
  }

  public static MapVinaAnimatorProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new MapVinaAnimatorProvider();
    }
    return INSTANCE;
  }

  MapVinaLatLngAnimator latLngAnimator(LatLng[] values, MapVinaAnimator.AnimationsValueChangeListener updateListener,
                                        int maxAnimationFps) {
    return new MapVinaLatLngAnimator(values, updateListener, maxAnimationFps);
  }

  MapVinaFloatAnimator floatAnimator(Float[] values, MapVinaAnimator.AnimationsValueChangeListener updateListener,
                                      int maxAnimationFps) {
    return new MapVinaFloatAnimator(values, updateListener, maxAnimationFps);
  }

  MapVinaCameraAnimatorAdapter cameraAnimator(Float[] values,
                                               MapVinaAnimator.AnimationsValueChangeListener updateListener,
                                               @Nullable MapVinaMap.CancelableCallback cancelableCallback) {
    return new MapVinaCameraAnimatorAdapter(values, updateListener, cancelableCallback);
  }

  MapVinaPaddingAnimator paddingAnimator(double[][] values,
                                        MapVinaAnimator.AnimationsValueChangeListener<double[]> updateListener,
                                        @Nullable MapVinaMap.CancelableCallback cancelableCallback) {
    return new MapVinaPaddingAnimator(values, updateListener, cancelableCallback);
  }

  /**
   * This animator is for the LocationComponent pulsing circle.
   *
   * @param updateListener the listener that is found in the {@link LocationAnimatorCoordinator}'s
   *                       listener array.
   * @param maxAnimationFps the max frames per second of the pulsing animation
   * @param pulseSingleDuration the number of milliseconds it takes for the animator to create
   *                            a single pulse.
   * @param pulseMaxRadius the max radius when the circle is finished with a single pulse.
   * @param pulseInterpolator the type of Android-system interpolator to use for
   *                                       the pulsing animation (linear, accelerate, bounce, etc.)
   * @return a built {@link PulsingLocationCircleAnimator} object.
   */
  PulsingLocationCircleAnimator pulsingCircleAnimator(MapVinaAnimator.AnimationsValueChangeListener updateListener,
                                                      int maxAnimationFps,
                                                      float pulseSingleDuration,
                                                      float pulseMaxRadius,
                                                      Interpolator pulseInterpolator) {
    PulsingLocationCircleAnimator pulsingLocationCircleAnimator =
        new PulsingLocationCircleAnimator(updateListener, maxAnimationFps, pulseMaxRadius);
    pulsingLocationCircleAnimator.setDuration((long) pulseSingleDuration);
    pulsingLocationCircleAnimator.setRepeatMode(ValueAnimator.RESTART);
    pulsingLocationCircleAnimator.setRepeatCount(ValueAnimator.INFINITE);
    pulsingLocationCircleAnimator.setInterpolator(pulseInterpolator);
    return pulsingLocationCircleAnimator;
  }
}
