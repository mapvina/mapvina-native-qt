package com.mapvina.android.location;

class AnimatorListenerHolder {
  @MapVinaAnimator.Type
  private final int animatorType;
  private final MapVinaAnimator.AnimationsValueChangeListener listener;

  AnimatorListenerHolder(@MapVinaAnimator.Type int animatorType,
                         MapVinaAnimator.AnimationsValueChangeListener listener) {
    this.animatorType = animatorType;
    this.listener = listener;
  }

  @MapVinaAnimator.Type
  public int getAnimatorType() {
    return animatorType;
  }

  public MapVinaAnimator.AnimationsValueChangeListener getListener() {
    return listener;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AnimatorListenerHolder that = (AnimatorListenerHolder) o;

    if (animatorType != that.animatorType) {
      return false;
    }
    return listener != null ? listener.equals(that.listener) : that.listener == null;
  }

  @Override
  public int hashCode() {
    int result = animatorType;
    result = 31 * result + (listener != null ? listener.hashCode() : 0);
    return result;
  }
}
