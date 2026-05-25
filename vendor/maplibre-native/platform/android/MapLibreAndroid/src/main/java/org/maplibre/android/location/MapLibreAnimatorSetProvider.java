package com.mapvina.android.location;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;

import java.util.List;

class MapVinaAnimatorSetProvider {
  private static MapVinaAnimatorSetProvider instance;

  private MapVinaAnimatorSetProvider() {
    // private constructor
  }

  static MapVinaAnimatorSetProvider getInstance() {
    if (instance == null) {
      instance = new MapVinaAnimatorSetProvider();
    }
    return instance;
  }

  void startAnimation(@NonNull List<Animator> animators, @NonNull Interpolator interpolator,
                      long duration) {
    AnimatorSet locationAnimatorSet = new AnimatorSet();
    locationAnimatorSet.playTogether(animators);
    locationAnimatorSet.setInterpolator(interpolator);
    locationAnimatorSet.setDuration(duration);
    locationAnimatorSet.start();
  }
}
