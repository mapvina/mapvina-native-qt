package com.mapvina.android.testapp.geometry;

import androidx.test.annotation.UiThreadTest;

import com.google.gson.JsonArray;
import com.mapvina.geojson.Feature;
import com.mapvina.geojson.Point;
import com.mapvina.geojson.Polygon;
import com.mapvina.android.geometry.LatLng;
import com.mapvina.android.style.expressions.Expression;
import com.mapvina.android.style.layers.PropertyFactory;
import com.mapvina.android.style.layers.SymbolLayer;
import com.mapvina.android.style.sources.GeoJsonSource;
import com.mapvina.android.testapp.action.MapVinaMapAction;
import com.mapvina.android.testapp.activity.EspressoTest;
import com.mapvina.android.testapp.utils.TestingAsyncUtils;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static com.mapvina.geojson.Feature.fromGeometry;
import static com.mapvina.geojson.FeatureCollection.fromFeatures;
import static com.mapvina.geojson.GeometryCollection.fromGeometries;
import static com.mapvina.geojson.LineString.fromLngLats;
import static com.mapvina.geojson.MultiLineString.fromLineString;
import static com.mapvina.geojson.MultiPolygon.fromPolygon;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertFalse;

/**
 * Instrumentation test to validate java geojson conversion to c++
 */
public class GeoJsonConversionTest extends EspressoTest {

  // Regression test for #12343
  @Test
  @UiThreadTest
  public void testEmptyFeatureCollection() {
    validateTestSetup();
    mapvinaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(fromGeometries(emptyList()))))
      )
    );
    mapvinaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testPointFeatureCollection() {
    validateTestSetup();
    mapvinaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(Point.fromLngLat(0.0, 0.0))))
      )
    );
    mapvinaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testMultiPointFeatureCollection() {
    validateTestSetup();
    mapvinaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(fromLngLats(emptyList()))))
      )
    );
    mapvinaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testPolygonFeatureCollection() {
    validateTestSetup();
    mapvinaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(Polygon.fromLngLats(emptyList()))))
      )
    );
    mapvinaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testMultiPolygonFeatureCollection() {
    validateTestSetup();
    mapvinaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(fromPolygon(Polygon.fromLngLats(emptyList())))))
      )
    );
    mapvinaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testLineStringFeatureCollection() {
    validateTestSetup();
    mapvinaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(fromLngLats(emptyList()))))
      )
    );
    mapvinaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }

  @Test
  @UiThreadTest
  public void testMultiLineStringFeatureCollection() {
    validateTestSetup();
    mapvinaMap.getStyle().addSource(
      new GeoJsonSource("test-id",
        fromFeatures(singletonList(fromGeometry(fromLineString(fromLngLats(emptyList())))))
      )
    );
    mapvinaMap.getStyle().addLayer(new SymbolLayer("test-id", "test-id"));
  }


  @Test
  public void testNegativeNumberPropertyConversion() {
    validateTestSetup();
    onView(isRoot()).perform(new MapVinaMapAction((uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng();
      Feature feature = Feature.fromGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));

      JsonArray foregroundJsonArray = new JsonArray();
      foregroundJsonArray.add(0f);
      foregroundJsonArray.add(-3f);
      feature.addProperty("property", foregroundJsonArray);

      GeoJsonSource source = new GeoJsonSource("source", feature);
      mapvinaMap.getStyle().addSource(source);

      SymbolLayer layer = new SymbolLayer("layer", "source")
        .withProperties(
          PropertyFactory.iconOffset(Expression.get("property")),
          PropertyFactory.iconImage("icon-zoo")
        );
      mapvinaMap.getStyle().addLayer(layer);

      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng)).isEmpty());
    }, mapvinaMap));
  }
}
