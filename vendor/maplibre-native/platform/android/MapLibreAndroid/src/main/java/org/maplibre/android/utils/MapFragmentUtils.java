package com.mapvina.android.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mapvina.android.constants.MapVinaConstants;
import com.mapvina.android.maps.MapFragment;
import com.mapvina.android.maps.SupportMapFragment;
import com.mapvina.android.maps.MapVinaMapOptions;

/**
 * MapFragment utility class.
 * <p>
 * Used to extract duplicate code between {@link MapFragment} and
 * {@link SupportMapFragment}.
 * </p>
 */
public class MapFragmentUtils {

  /**
   * Convert MapVinaMapOptions to a bundle of fragment arguments.
   *
   * @param options The MapVinaMapOptions to convert
   * @return a bundle of converted fragment arguments
   */
  @NonNull
  public static Bundle createFragmentArgs(MapVinaMapOptions options) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(MapVinaConstants.FRAG_ARG_MAPVINAMAPOPTIONS, options);
    return bundle;
  }

  /**
   * Convert a bundle of fragment arguments to MapVinaMapOptions.
   *
   * @param context The context of the activity hosting the fragment
   * @param args    The fragment arguments
   * @return converted MapVinaMapOptions
   */
  @Nullable
  public static MapVinaMapOptions resolveArgs(@NonNull Context context, @Nullable Bundle args) {
    MapVinaMapOptions options;
    if (args != null && args.containsKey(MapVinaConstants.FRAG_ARG_MAPVINAMAPOPTIONS)) {
      options = args.getParcelable(MapVinaConstants.FRAG_ARG_MAPVINAMAPOPTIONS);
    } else {
      // load default options
      options = MapVinaMapOptions.createFromAttributes(context);
    }
    return options;
  }
}
