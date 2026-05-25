package com.mapvina.android.maps;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.mapvina.android.annotations.InfoWindow;
import com.mapvina.android.annotations.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for managing InfoWindows shown on the Map.
 * <p>
 * Maintains a {@link List} of opened {@link InfoWindow} and tracks configurations as
 * allowConcurrentMultipleInfoWindows which allows to have multiple {@link InfoWindow} open at the
 * same time. Responsible for managing listeners as
 * {@link MapVinaMap.OnInfoWindowClickListener} and
 * {@link MapVinaMap.OnInfoWindowLongClickListener}.
 * </p>
 */
class InfoWindowManager {

  private final List<InfoWindow> infoWindows = new ArrayList<>();

  @Nullable
  private MapVinaMap.InfoWindowAdapter infoWindowAdapter;
  private boolean allowConcurrentMultipleInfoWindows;

  @Nullable
  private MapVinaMap.OnInfoWindowClickListener onInfoWindowClickListener;
  @Nullable
  private MapVinaMap.OnInfoWindowLongClickListener onInfoWindowLongClickListener;
  @Nullable
  private MapVinaMap.OnInfoWindowCloseListener onInfoWindowCloseListener;

  void update() {
    if (!infoWindows.isEmpty()) {
      for (InfoWindow infoWindow : infoWindows) {
        infoWindow.update();
      }
    }
  }

  void setInfoWindowAdapter(@Nullable MapVinaMap.InfoWindowAdapter infoWindowAdapter) {
    this.infoWindowAdapter = infoWindowAdapter;
  }

  @Nullable
  MapVinaMap.InfoWindowAdapter getInfoWindowAdapter() {
    return infoWindowAdapter;
  }

  void setAllowConcurrentMultipleOpenInfoWindows(boolean allow) {
    allowConcurrentMultipleInfoWindows = allow;
  }

  boolean isAllowConcurrentMultipleOpenInfoWindows() {
    return allowConcurrentMultipleInfoWindows;
  }

  boolean isInfoWindowValidForMarker(@Nullable Marker marker) {
    return marker != null && (!TextUtils.isEmpty(marker.getTitle()) || !TextUtils.isEmpty(marker.getSnippet()));
  }

  void setOnInfoWindowClickListener(@Nullable MapVinaMap.OnInfoWindowClickListener listener) {
    onInfoWindowClickListener = listener;
  }

  @Nullable
  MapVinaMap.OnInfoWindowClickListener getOnInfoWindowClickListener() {
    return onInfoWindowClickListener;
  }

  void setOnInfoWindowLongClickListener(@Nullable MapVinaMap.OnInfoWindowLongClickListener listener) {
    onInfoWindowLongClickListener = listener;
  }

  @Nullable
  MapVinaMap.OnInfoWindowLongClickListener getOnInfoWindowLongClickListener() {
    return onInfoWindowLongClickListener;
  }

  void setOnInfoWindowCloseListener(@Nullable MapVinaMap.OnInfoWindowCloseListener listener) {
    onInfoWindowCloseListener = listener;
  }

  @Nullable
  MapVinaMap.OnInfoWindowCloseListener getOnInfoWindowCloseListener() {
    return onInfoWindowCloseListener;
  }

  public void add(InfoWindow infoWindow) {
    infoWindows.add(infoWindow);
  }
}
