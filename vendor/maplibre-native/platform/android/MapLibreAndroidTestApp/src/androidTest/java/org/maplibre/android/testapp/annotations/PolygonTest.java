package com.mapvina.android.testapp.annotations;

import android.graphics.Color;

import com.mapvina.android.annotations.Polygon;
import com.mapvina.android.annotations.PolygonOptions;
import com.mapvina.android.geometry.LatLng;
import com.mapvina.android.testapp.activity.EspressoTest;

import org.junit.Ignore;
import org.junit.Test;

import static com.mapvina.android.testapp.action.MapVinaMapAction.invoke;
import static org.junit.Assert.assertEquals;

public class PolygonTest extends EspressoTest {

  @Test
  @Ignore
  public void addPolygonTest() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLngOne = new LatLng();
      LatLng latLngTwo = new LatLng(1, 0);
      LatLng latLngThree = new LatLng(1, 1);

      assertEquals("Polygons should be empty", 0, mapvinaMap.getPolygons().size());

      final PolygonOptions options = new PolygonOptions();
      options.strokeColor(Color.BLUE);
      options.fillColor(Color.RED);
      options.add(latLngOne);
      options.add(latLngTwo);
      options.add(latLngThree);
      Polygon polygon = mapvinaMap.addPolygon(options);

      assertEquals("Polygons should be 1", 1, mapvinaMap.getPolygons().size());
      assertEquals("Polygon id should be 0", 0, polygon.getId());
      assertEquals("Polygon points size should match", 3, polygon.getPoints().size());
      assertEquals("Polygon stroke color should match", Color.BLUE, polygon.getStrokeColor());
      assertEquals("Polygon target should match", Color.RED, polygon.getFillColor());
      mapvinaMap.clear();
      assertEquals("Polygons should be empty", 0, mapvinaMap.getPolygons().size());
    });
  }
}
