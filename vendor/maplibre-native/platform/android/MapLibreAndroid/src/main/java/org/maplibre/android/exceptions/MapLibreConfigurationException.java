package com.mapvina.android.exceptions;

import com.mapvina.android.MapVina;
import com.mapvina.android.WellKnownTileServer;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * A MapboxConfigurationException is thrown by MapVinaMap when the SDK hasn't been properly initialised.
 * <p>
 * This occurs either when {@link MapVina} is not correctly initialised or the provided apiKey
 * through {@link MapVina#getInstance(Context, String, WellKnownTileServer)} isn't valid.
 * </p>
 *
 * @see MapVina#getInstance(Context, String,  WellKnownTileServer)
 */
public class MapVinaConfigurationException extends RuntimeException {

  /**
   * Creates a MapVina configuration exception thrown by MapVinaMap when the SDK hasn't been properly initialised.
   */
  public MapVinaConfigurationException() {
    super("\nUsing MapView requires calling MapVina.getInstance(Context context, String apiKey, "
            + "WellKnownTileServer wellKnownTileServer) before inflating or creating the view.");
  }

  /**
   * Creates a MapVina configuration exception thrown by MapVinaMap when the SDK hasn't been properly initialised.
   */
  public MapVinaConfigurationException(@NonNull String message) {
    super(message);
  }
}
