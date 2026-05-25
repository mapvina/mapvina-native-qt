package com.mapvina.android.maps;


import androidx.annotation.NonNull;

import com.mapvina.android.annotations.Polyline;
import com.mapvina.android.annotations.PolylineOptions;

import java.util.List;

/**
 * Interface that defines convenient methods for working with a {@link Polyline}'s collection.
 */
interface Polylines {
  Polyline addBy(@NonNull PolylineOptions polylineOptions, @NonNull MapVinaMap mapvinaMap);

  List<Polyline> addBy(@NonNull List<PolylineOptions> polylineOptionsList, @NonNull MapVinaMap mapvinaMap);

  void update(Polyline polyline);

  List<Polyline> obtainAll();
}
