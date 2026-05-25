package com.mapvina.android.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;

import com.mapvina.android.maps.MapVinaMap;

class MapVinaCameraAnimatorAdapter extends MapVinaFloatAnimator {

  MapVinaCameraAnimatorAdapter(@NonNull @Size(min = 2) Float[] values,
                                AnimationsValueChangeListener updateListener,
                                @Nullable MapVinaMap.CancelableCallback cancelableCallback) {
    super(values, updateListener, Integer.MAX_VALUE);
    addListener(new MapVinaAnimatorListener(cancelableCallback));
  }
}
