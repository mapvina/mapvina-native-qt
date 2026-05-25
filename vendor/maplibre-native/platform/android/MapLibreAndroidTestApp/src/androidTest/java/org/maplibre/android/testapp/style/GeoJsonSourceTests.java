package com.mapvina.android.testapp.style;

import org.junit.Ignore;
import com.mapvina.android.style.sources.CannotAddSourceException;
import com.mapvina.geojson.Feature;
import com.mapvina.geojson.FeatureCollection;
import com.mapvina.geojson.Point;
import com.mapvina.android.geometry.LatLng;
import com.mapvina.android.style.layers.CircleLayer;
import com.mapvina.android.style.layers.Layer;
import com.mapvina.android.style.sources.GeoJsonSource;
import com.mapvina.android.testapp.R;
import com.mapvina.android.testapp.action.MapVinaMapAction;
import com.mapvina.android.testapp.activity.EspressoTest;
import com.mapvina.android.testapp.utils.ResourceUtils;
import com.mapvina.android.testapp.utils.TestingAsyncUtils;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RawRes;
import androidx.test.espresso.ViewAction;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link GeoJsonSource}
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class GeoJsonSourceTests extends EspressoTest {

  @Test
  public void testFeatureCollection() {
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      GeoJsonSource source = null;
      source = new GeoJsonSource("source", FeatureCollection
              .fromJson(ResourceUtils.readRawResource(rule.getActivity(), R.raw.test_feature_collection)));
      mapvinaMap.getStyle().addSource(source);
      mapvinaMap.getStyle().addLayer(new CircleLayer("layer", source.getId()));
    });
  }

  @Test
  public void testPointGeometry() {
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      GeoJsonSource source = new GeoJsonSource("source", Point.fromLngLat(0d, 0d));
      mapvinaMap.getStyle().addSource(source);
      mapvinaMap.getStyle().addLayer(new CircleLayer("layer", source.getId()));
    });
  }

  @Test
  public void testFeatureProperties() {
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      GeoJsonSource source = null;
      source = new GeoJsonSource("source",
              ResourceUtils.readRawResource(rule.getActivity(), R.raw.test_feature_properties));
      mapvinaMap.getStyle().addSource(source);
      mapvinaMap.getStyle().addLayer(new CircleLayer("layer", source.getId()));
    });
  }

  @Test
  public void testUpdateCoalescing() {
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      GeoJsonSource source = new GeoJsonSource("source");
      mapvinaMap.getStyle().addSource(source);
      mapvinaMap.getStyle().addLayer(new CircleLayer("layer", source.getId()));

      source.setGeoJson(Point.fromLngLat(0, 0));
      source.setGeoJson(Point.fromLngLat(-25, -25));
      source.setGeoJson(ResourceUtils.readRawResource(rule.getActivity(), R.raw.test_feature_properties));

      source.setGeoJson(Point.fromLngLat(20, 55));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);
      assertEquals(1, mapvinaMap.queryRenderedFeatures(
              mapvinaMap.getProjection().toScreenLocation(
                      new LatLng(55, 20)), "layer").size());
    });
  }

  @Test
  public void testUpdateCoalescingSync() {
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      GeoJsonSource source = new GeoJsonSource("source");
      mapvinaMap.getStyle().addSource(source);
      mapvinaMap.getStyle().addLayer(new CircleLayer("layer", source.getId()));

      source.setGeoJsonSync(Point.fromLngLat(0, 0));
      source.setGeoJsonSync(Point.fromLngLat(-25, -25));
      source.setGeoJsonSync(ResourceUtils.readRawResource(rule.getActivity(), R.raw.test_feature_properties));

      source.setGeoJsonSync(Point.fromLngLat(20, 55));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);
      assertEquals(1, mapvinaMap.queryRenderedFeatures(
              mapvinaMap.getProjection().toScreenLocation(
                      new LatLng(55, 20)), "layer").size());
    });
  }

  @Test
  public void testClearCollectionDuringConversion() {
    // https://github.com/mapbox/mapbox-gl-native/issues/14565
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      for (int j = 0; j < 1000; j++) {
        List<Feature> features = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
          features.add(Feature.fromGeometry(Point.fromLngLat(0, 0)));
        }
        mapvinaMap.getStyle().addSource(new GeoJsonSource("source" + j, FeatureCollection.fromFeatures(features)));
        features.clear();
      }
    });
  }

  @Test
  public void testPointFeature() {
    testFeatureFromResource(R.raw.test_point_feature);
  }

  @Test
  public void testLineStringFeature() {
    testFeatureFromResource(R.raw.test_line_string_feature);
  }

  @Test
  public void testPolygonFeature() {
    testFeatureFromResource(R.raw.test_polygon_feature);
  }

  @Test
  public void testPolygonWithHoleFeature() {
    testFeatureFromResource(R.raw.test_polygon_with_hole_feature);
  }

  @Test
  public void testMultiPointFeature() {
    testFeatureFromResource(R.raw.test_multi_point_feature);
  }

  @Test
  public void testMultiLineStringFeature() {
    testFeatureFromResource(R.raw.test_multi_line_string_feature);
  }

  @Test
  public void testMultiPolygonFeature() {
    testFeatureFromResource(R.raw.test_multi_polygon_feature);
  }

  protected void testFeatureFromResource(final @RawRes int resource) {
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      GeoJsonSource source = new GeoJsonSource("source");
      mapvinaMap.getStyle().addSource(source);
      Layer layer = new CircleLayer("layer", source.getId());
      mapvinaMap.getStyle().addLayer(layer);

      source.setGeoJson(Feature.fromJson(ResourceUtils.readRawResource(rule.getActivity(), resource)));

      mapvinaMap.getStyle().removeLayer(layer);
      mapvinaMap.getStyle().removeSource(source);
    });
  }

  @Test
  public void testPointFeatureSync() {
    testFeatureFromResourceSync(R.raw.test_point_feature);
  }

  @Test
  public void testLineStringFeatureSync() {
    testFeatureFromResourceSync(R.raw.test_line_string_feature);
  }

  @Test
  public void testPolygonFeatureSync() {
    testFeatureFromResourceSync(R.raw.test_polygon_feature);
  }

  @Test
  public void testPolygonWithHoleFeatureSync() {
    testFeatureFromResourceSync(R.raw.test_polygon_with_hole_feature);
  }

  @Test
  public void testMultiPointFeatureSync() {
    testFeatureFromResourceSync(R.raw.test_multi_point_feature);
  }

  @Test
  public void testMultiLineStringFeatureSync() {
    testFeatureFromResourceSync(R.raw.test_multi_line_string_feature);
  }

  @Test
  public void testMultiPolygonFeatureSync() {
    testFeatureFromResourceSync(R.raw.test_multi_polygon_feature);
  }

  protected void testFeatureFromResourceSync(final @RawRes int resource) {
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      GeoJsonSource source = new GeoJsonSource("source");
      mapvinaMap.getStyle().addSource(source);
      Layer layer = new CircleLayer("layer", source.getId());
      mapvinaMap.getStyle().addLayer(layer);

      source.setGeoJsonSync(Feature.fromJson(ResourceUtils.readRawResource(rule.getActivity(), resource)));

      mapvinaMap.getStyle().removeLayer(layer);
      mapvinaMap.getStyle().removeSource(source);
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/3493")
  public void testDuplicateSourceDuringAsyncSetGeoJson() {
    // regression test for segfault when setting GeoJSON contents
    // for a source that has been deleted
    // https://github.com/mapvina/mapvina-native/issues/3493
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      String geoJsonString = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[0,0]}}";
      for (int i = 0; i < 1000; i++) {
        // Create a GeoJSON source with the same ID each iteration
        GeoJsonSource source = new GeoJsonSource("source");
        try {
          mapvinaMap.getStyle().addSource(source);
        } catch (CannotAddSourceException ex) {
          // ignore, this is expected
        }

        // Schedule an async update via setGeoJson(String)
        source.setGeoJson(geoJsonString);
      }
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/3493")
  public void testDuplicateSourceDuringSyncSetGeoJson() {
    // regression test for segfault when setting GeoJSON contents
    // for a source that has been deleted
    // https://github.com/mapvina/mapvina-native/issues/3493
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      String geoJsonString = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[0,0]}}";
      for (int i = 0; i < 1000; i++) {
        // Create a GeoJSON source with the same ID each iteration
        GeoJsonSource source = new GeoJsonSource("source");
        try {
          mapvinaMap.getStyle().addSource(source);
        } catch (CannotAddSourceException ex) {
          // ignore, this is expected
        }

        // Schedule a sync update via setGeoJson(String)
        source.setGeoJsonSync(geoJsonString);
      }
    });
  }

  public abstract class BaseViewAction implements ViewAction {

    @Override
    public Matcher<View> getConstraints() {
      return isDisplayed();
    }

    @Override
    public String getDescription() {
      return getClass().getSimpleName();
    }

  }
}
