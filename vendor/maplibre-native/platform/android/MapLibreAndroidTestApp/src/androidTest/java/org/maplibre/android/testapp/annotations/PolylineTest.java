package com.mapvina.android.testapp.annotations;

import android.graphics.Color;

import com.mapvina.android.annotations.Polyline;
import com.mapvina.android.annotations.PolylineOptions;
import com.mapvina.android.geometry.LatLng;
import com.mapvina.android.testapp.activity.EspressoTest;

import org.junit.Ignore;
import org.junit.Test;

import static com.mapvina.android.testapp.action.MapVinaMapAction.invoke;
import static org.junit.Assert.assertEquals;

public class PolylineTest extends EspressoTest {

  @Test
  @Ignore
  public void addPolylineTest() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLngOne = new LatLng();
      LatLng latLngTwo = new LatLng(1, 0);

      assertEquals("Polygons should be empty", 0, mapvinaMap.getPolygons().size());

      final PolylineOptions options = new PolylineOptions();
      options.color(Color.BLUE);
      options.add(latLngOne);
      options.add(latLngTwo);
      Polyline polyline = mapvinaMap.addPolyline(options);

      assertEquals("Polylines should be 1", 1, mapvinaMap.getPolylines().size());
      assertEquals("Polyline id should be 0", 0, polyline.getId());
      assertEquals("Polyline points size should match", 2, polyline.getPoints().size());
      assertEquals("Polyline stroke color should match", Color.BLUE, polyline.getColor());
      mapvinaMap.clear();
      assertEquals("Polyline should be empty", 0, mapvinaMap.getPolylines().size());
    });
  }
}
