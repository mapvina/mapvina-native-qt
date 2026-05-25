package com.mapvina.android.maps;

import androidx.annotation.NonNull;

/**
 * Interface definition for a callback to be invoked when the map is ready to be used.
 * <p>
 * Once an instance of this interface is set on a {@link MapFragment} or {@link MapView} object,
 * the onMapReady(MapVinaMap) method is triggered when the map is ready to be used and provides an instance of
 * {@link MapVinaMap}.
 * </p>
 */
public interface OnMapReadyCallback {

  /**
   * Called when the map is ready to be used.
   *
   * @param mapvinaMap An instance of MapVinaMap associated with the {@link MapFragment} or
   *                  {@link MapView} that defines the callback.
   */
  void onMapReady(@NonNull MapVinaMap mapvinaMap);
}
