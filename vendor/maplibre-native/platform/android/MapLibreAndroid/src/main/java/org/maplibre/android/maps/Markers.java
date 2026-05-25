package com.mapvina.android.maps;

import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.mapvina.android.annotations.BaseMarkerOptions;
import com.mapvina.android.annotations.Marker;

import java.util.List;

/**
 * Interface that defines convenient methods for working with a {@link Marker}'s collection.
 */
interface Markers {
  Marker addBy(@NonNull BaseMarkerOptions markerOptions, @NonNull MapVinaMap mapvinaMap);

  List<Marker> addBy(@NonNull List<? extends BaseMarkerOptions> markerOptionsList, @NonNull MapVinaMap mapvinaMap);

  void update(@NonNull Marker updatedMarker, @NonNull MapVinaMap mapvinaMap);

  List<Marker> obtainAll();

  @NonNull
  List<Marker> obtainAllIn(@NonNull RectF rectangle);

  void reload();
}
