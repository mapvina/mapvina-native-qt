package com.mapvina.android.maps;


import androidx.annotation.NonNull;

import com.mapvina.android.annotations.Polygon;
import com.mapvina.android.annotations.PolygonOptions;

import java.util.List;

/**
 * Interface that defines convenient methods for working with a {@link Polygon}'s collection.
 */
interface Polygons {
  Polygon addBy(@NonNull PolygonOptions polygonOptions, @NonNull MapVinaMap mapvinaMap);

  List<Polygon> addBy(@NonNull List<PolygonOptions> polygonOptionsList, @NonNull MapVinaMap mapvinaMap);

  void update(Polygon polygon);

  List<Polygon> obtainAll();
}
