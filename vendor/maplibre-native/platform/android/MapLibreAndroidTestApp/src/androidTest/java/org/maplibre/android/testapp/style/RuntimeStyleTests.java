package com.mapvina.android.testapp.style;

import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.mapvina.android.style.layers.CannotAddLayerException;
import com.mapvina.android.style.layers.CircleLayer;
import com.mapvina.android.style.layers.FillLayer;
import com.mapvina.android.style.layers.Layer;
import com.mapvina.android.style.layers.LineLayer;
import com.mapvina.android.style.layers.Property;
import com.mapvina.android.style.layers.PropertyFactory;
import com.mapvina.android.style.sources.CannotAddSourceException;
import com.mapvina.android.style.sources.GeoJsonSource;
import com.mapvina.android.style.sources.RasterSource;
import com.mapvina.android.style.sources.Source;
import com.mapvina.android.style.sources.VectorSource;
import com.mapvina.android.testapp.R;
import com.mapvina.android.testapp.activity.EspressoTest;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.mapvina.android.testapp.action.MapVinaMapAction.invoke;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Basic smoke tests for Layer and Source
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class RuntimeStyleTests extends EspressoTest {

  @Test
  public void testListLayers() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        List<Layer> layers = mapvinaMap.getStyle().getLayers();
        assertNotNull(layers);
        assertTrue(layers.size() > 0);
        for (Layer layer : layers) {
          assertNotNull(layer);
        }
      }

    });
  }

  @Test
  public void testGetAddRemoveLayer() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new AddRemoveLayerAction());
  }

  @Test
  public void testAddLayerAbove() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        List<Layer> layers = Objects.requireNonNull(mapvinaMap.getStyle()).getLayers();

        Source source = mapvinaMap.getStyle().getSources().stream()
          .filter(s -> s.getId().equals("openmaptiles")).findAny().get();

        // Test inserting with invalid above-id
        try {
          mapvinaMap.getStyle().addLayerAbove(
            new CircleLayer("invalid-id-layer-test", source.getId()), "no-such-layer-here-man"
          );
          fail("Should have thrown exception");
        } catch (CannotAddLayerException ex) {
          // Yeah
          assertNotNull(ex.getMessage());
        }

        // Insert as last
        CircleLayer last = new CircleLayer("this is the last one", source.getId());
        mapvinaMap.getStyle().addLayerAbove(last, layers.get(layers.size() - 1).getId());
        layers = mapvinaMap.getStyle().getLayers();
        assertEquals(last.getId(), layers.get(layers.size() - 1).getId());

        // Insert
        CircleLayer second = new CircleLayer("this is the second one", source.getId());
        mapvinaMap.getStyle().addLayerAbove(second, layers.get(0).getId());
        layers = mapvinaMap.getStyle().getLayers();
        assertEquals(second.getId(), layers.get(1).getId());
      }
    });
  }

  @Test
  public void testRemoveLayerAt() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        // Remove by index
        Layer firstLayer = mapvinaMap.getStyle().getLayers().get(0);
        boolean removed = mapvinaMap.getStyle().removeLayerAt(0);
        assertTrue(removed);
        assertNotNull(firstLayer);

        // Test remove by index bounds checks
        Timber.i("Remove layer at index > size");
        assertFalse(mapvinaMap.getStyle().removeLayerAt(Integer.MAX_VALUE));
      }
    });
  }

  @Test
  public void testAddLayerAt() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        List<Layer> layers = Objects.requireNonNull(mapvinaMap.getStyle()).getLayers();
        Source source = mapvinaMap.getStyle().getSources().stream()
          .filter(s -> s.getId().equals("openmaptiles")).findAny().get();

        // Test inserting out of range
        try {
          mapvinaMap.getStyle().addLayerAt(new CircleLayer("invalid-id-layer-test", source.getId()), layers.size());
          fail("Should have thrown exception");
        } catch (CannotAddLayerException ex) {
          // Yeah
          assertNotNull(ex.getMessage());
        }

        // Insert at current last position
        CircleLayer last = new CircleLayer("this is the last one", source.getId());
        mapvinaMap.getStyle().addLayerAt(last, layers.size() - 1);
        layers = mapvinaMap.getStyle().getLayers();
        assertEquals(last.getId(), layers.get(layers.size() - 2).getId());

        // Insert at start
        CircleLayer second = new CircleLayer("this is the first one", source.getId());
        mapvinaMap.getStyle().addLayerAt(second, 0);
        layers = mapvinaMap.getStyle().getLayers();
        assertEquals(second.getId(), layers.get(0).getId());
      }
    });
  }


  @Test
  public void testListSources() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        List<Source> sources = mapvinaMap.getStyle().getSources();
        assertNotNull(sources);
        assertTrue(sources.size() > 0);
        for (Source source : sources) {
          assertNotNull(source);
        }
      }

    });
  }

  @Test
  public void testAddRemoveSource() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      mapvinaMap.getStyle().addSource(new VectorSource("my-source", "maptiler://sources/hillshades"));
      mapvinaMap.getStyle().removeSource("my-source");

      // Add initial source
      mapvinaMap.getStyle().addSource(new VectorSource("my-source", "maptiler://sources/hillshades"));

      // Remove
      boolean removeOk = mapvinaMap.getStyle().removeSource("my-source");
      assertTrue(removeOk);
      assertNull(mapvinaMap.getStyle().getLayer("my-source"));

      // Add
      Source source = new VectorSource("my-source", "maptiler://sources/hillshades");
      mapvinaMap.getStyle().addSource(source);

      // Remove, preserving the reference
      mapvinaMap.getStyle().removeSource(source);

      // Re-add the reference...
      mapvinaMap.getStyle().addSource(source);

      // Ensure it's there
      Assert.assertNotNull(mapvinaMap.getStyle().getSource(source.getId()));

      // Test adding a duplicate source
      try {
        Source source2 = new VectorSource("my-source", "maptiler://sources/hillshades");
        mapvinaMap.getStyle().addSource(source2);
        fail("Should not have been allowed to add a source with a duplicate id");
      } catch (CannotAddSourceException cannotAddSourceException) {
        // OK
      }
    });

  }

  @Test
  public void testVectorSourceUrlGetter() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      VectorSource source = new VectorSource("my-source", "maptiler://sources/hillshades");
      mapvinaMap.getStyle().addSource(source);
      assertEquals("maptiler://sources/hillshades", source.getUri());
    });
  }

  @Test
  public void testRasterSourceUrlGetter() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      RasterSource source = new RasterSource("my-source", "maptiler://sources/hillshades");
      mapvinaMap.getStyle().addSource(source);
      assertEquals("maptiler://sources/hillshades", source.getUri());
    });
  }

  @Test
  public void testGeoJsonSourceUrlGetter() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      GeoJsonSource source = new GeoJsonSource("my-source");
      mapvinaMap.getStyle().addSource(source);
      assertNull(source.getUri());
      source.setUri("http://mapbox.com/my-file.json");
      assertEquals("http://mapbox.com/my-file.json", source.getUri());
    });
  }

  @Test
  public void testRemoveSourceInUse() {
    validateTestSetup();

    onView(withId(R.id.mapView)).perform(new BaseViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        mapvinaMap.getStyle().addSource(new VectorSource("my-source", "maptiler://sources/hillshades"));
        mapvinaMap.getStyle().addLayer(new LineLayer("my-layer", "my-source"));
        mapvinaMap.getStyle().removeSource("my-source");
        assertNotNull(mapvinaMap.getStyle().getSource("my-source"));
      }

    });
  }

  @Test
  public void testRemoveNonExistingSource() {
    invoke(mapvinaMap, (uiController, mapvinaMap) -> mapvinaMap.getStyle().removeSource("source"));
  }

  @Test
  public void testRemoveNonExistingLayer() {
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      assertFalse(mapvinaMap.getStyle().removeLayer("layer"));
      assertFalse(mapvinaMap.getStyle().removeLayerAt(mapvinaMap.getStyle().getLayers().size() + 1));
      assertFalse(mapvinaMap.getStyle().removeLayerAt(-1));
    });
  }

  @Test
  public void testRemoveExistingLayer() {
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      Layer firstLayer = mapvinaMap.getStyle().getLayers().get(0);
      assertTrue(mapvinaMap.getStyle().removeLayer(firstLayer));

      firstLayer = mapvinaMap.getStyle().getLayers().get(0);
      assertTrue(mapvinaMap.getStyle().removeLayer(firstLayer.getId()));

      assertTrue(mapvinaMap.getStyle().removeLayerAt(0));
    });
  }

  /**
   * https://github.com/mapbox/mapbox-gl-native/issues/7973
   */
  @Test
  public void testQueryRenderedFeaturesInputHandling() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        String[] layerIds = new String[600];
        for (int i = 0; i < layerIds.length; i++) {
          layerIds[i] = "layer-" + i;
        }
        mapvinaMap.queryRenderedFeatures(new PointF(100, 100), layerIds);
      }

    });
  }

  private class AddRemoveLayerAction extends BaseViewAction {

    @Override
    public void perform(UiController uiController, View view) {
      // Get initial
      assertNotNull(mapvinaMap.getStyle().getLayer("building"));

      // Remove
      boolean removed = mapvinaMap.getStyle().removeLayer("building");
      assertTrue(removed);
      assertNull(mapvinaMap.getStyle().getLayer("building"));

      // Add
      FillLayer layer = new FillLayer("building", "composite");
      layer.setSourceLayer("building");
      mapvinaMap.getStyle().addLayer(layer);
      assertNotNull(mapvinaMap.getStyle().getLayer("building"));

      // Assure the reference still works
      layer.setProperties(PropertyFactory.visibility(Property.VISIBLE));

      // Remove, preserving the reference
      mapvinaMap.getStyle().removeLayer(layer);

      // Property setters should still work
      layer.setProperties(PropertyFactory.fillColor(Color.RED));

      // Re-add the reference...
      mapvinaMap.getStyle().addLayer(layer);

      // Ensure it's there
      Assert.assertNotNull(mapvinaMap.getStyle().getLayer(layer.getId()));

      // Test adding a duplicate layer
      try {
        mapvinaMap.getStyle().addLayer(new FillLayer("building", "composite"));
        fail("Should not have been allowed to add a layer with a duplicate id");
      } catch (CannotAddLayerException cannotAddLayerException) {
        // OK
      }
    }
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
