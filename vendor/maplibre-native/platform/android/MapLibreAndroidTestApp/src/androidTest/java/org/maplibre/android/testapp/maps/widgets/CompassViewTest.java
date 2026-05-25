package com.mapvina.android.testapp.maps.widgets;

import com.mapvina.android.camera.CameraPosition;
import com.mapvina.android.camera.CameraUpdateFactory;
import com.mapvina.android.geometry.LatLng;
import com.mapvina.android.testapp.action.WaitAction;
import com.mapvina.android.testapp.activity.EspressoTest;
import com.mapvina.android.testapp.utils.TestConstants;

import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static com.mapvina.android.testapp.action.MapVinaMapAction.invoke;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

public class CompassViewTest extends EspressoTest {

  @Test
  public void testDefault() {
    validateTestSetup();
    onView(withTagValue(is("compassView"))).check(matches(not(isDisplayed())));
  }

  @Test
  @Ignore("No explanation given")
  public void testVisible() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      mapvinaMap.moveCamera(CameraUpdateFactory.newCameraPosition(
        new CameraPosition.Builder()
          .bearing(45)
          .zoom(1)
          .target(new LatLng())
            .build()
      ));
      uiController.loopMainThreadForAtLeast(500);
    });
    onView(withTagValue(is("compassView"))).check(matches(isDisplayed()));
  }

  @Test
  @Ignore("No explanation given")
  public void testClick() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> mapvinaMap.moveCamera(CameraUpdateFactory.newCameraPosition(
      new CameraPosition.Builder()
        .bearing(45)
        .zoom(1)
        .target(new LatLng())
        .build()
    )));
    onView(withTagValue(is("compassView"))).perform(click());
    WaitAction.invoke(500);
    onView(withTagValue(is("compassView"))).check(matches(not(isDisplayed())));
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      CameraPosition cameraPosition = mapvinaMap.getCameraPosition();
      assertEquals("Camera bearing should face north, ", 0, cameraPosition.bearing, TestConstants.BEARING_DELTA);
    });
  }
}
