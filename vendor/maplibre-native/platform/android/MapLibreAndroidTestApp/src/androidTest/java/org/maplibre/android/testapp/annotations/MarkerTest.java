package com.mapvina.android.testapp.annotations;

import com.mapvina.android.annotations.Marker;
import com.mapvina.android.annotations.MarkerOptions;
import com.mapvina.android.geometry.LatLng;
import com.mapvina.android.testapp.action.MapVinaMapAction;
import com.mapvina.android.testapp.activity.EspressoTest;
import com.mapvina.android.testapp.utils.TestConstants;

import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.mapvina.android.testapp.action.MapVinaMapAction.invoke;
import static org.junit.Assert.assertEquals;

public class MarkerTest extends EspressoTest {

  private Marker marker;

  @Test
  @Ignore
  public void addMarkerTest() {
    validateTestSetup();
    MapVinaMapAction.invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      assertEquals("Markers should be empty", 0, mapvinaMap.getMarkers().size());

      MarkerOptions options = new MarkerOptions();
      options.setPosition(new LatLng());
      options.setSnippet(TestConstants.TEXT_MARKER_SNIPPET);
      options.setTitle(TestConstants.TEXT_MARKER_TITLE);
      marker = mapvinaMap.addMarker(options);

      assertEquals("Markers size should be 1, ", 1, mapvinaMap.getMarkers().size());
      assertEquals("Marker id should be 0", 0, marker.getId());
      assertEquals("Marker target should match", new LatLng(), marker.getPosition());
      assertEquals("Marker snippet should match", TestConstants.TEXT_MARKER_SNIPPET, marker.getSnippet());
      assertEquals("Marker target should match", TestConstants.TEXT_MARKER_TITLE, marker.getTitle());
      mapvinaMap.clear();
      assertEquals("Markers should be empty", 0, mapvinaMap.getMarkers().size());
    });
  }

  @Test
  @Ignore
  public void showInfoWindowTest() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      final MarkerOptions options = new MarkerOptions();
      options.setPosition(new LatLng());
      options.setSnippet(TestConstants.TEXT_MARKER_SNIPPET);
      options.setTitle(TestConstants.TEXT_MARKER_TITLE);
      marker = mapvinaMap.addMarker(options);
      mapvinaMap.selectMarker(marker);
    });
    onView(withText(TestConstants.TEXT_MARKER_TITLE)).check(matches(isDisplayed()));
    onView(withText(TestConstants.TEXT_MARKER_SNIPPET)).check(matches(isDisplayed()));
  }

}
