package com.mapvina.android.testapp.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.core.content.res.ResourcesCompat;
import androidx.test.annotation.UiThreadTest;

import com.mapvina.android.annotations.Icon;
import com.mapvina.android.annotations.IconFactory;
import com.mapvina.android.annotations.Marker;
import com.mapvina.android.annotations.MarkerOptions;
import com.mapvina.android.geometry.LatLng;
import com.mapvina.android.maps.IconManagerResolver;
import com.mapvina.android.testapp.R;
import com.mapvina.android.testapp.activity.EspressoTest;
import com.mapvina.android.testapp.utils.IconUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * Tests integration between Icons and Markers
 */
public class IconTest extends EspressoTest {

  private Map<Icon, Integer> iconMap;

  @Before
  public void beforeTest() {
    super.beforeTest();
    iconMap = new IconManagerResolver(mapvinaMap).getIconMap();
  }

  @Test
  @UiThreadTest
  public void testAddSameIconMarker() {
    validateTestSetup();
    Icon defaultMarker = IconFactory.getInstance(rule.getActivity()).defaultMarker();
    mapvinaMap.addMarker(new MarkerOptions().position(new LatLng()));
    mapvinaMap.addMarker(new MarkerOptions().position(new LatLng(1, 1)));
    assertEquals(1, iconMap.size());
    assertEquals(2, iconMap.get(defaultMarker), 0);
  }

  @Test
  @UiThreadTest
  public void testAddDifferentIconMarker() {
    validateTestSetup();
    Icon icon = IconFactory.getInstance(rule.getActivity()).fromResource(R.drawable.mapvina_compass_icon);
    mapvinaMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng()));
    mapvinaMap.addMarker(new MarkerOptions().position(new LatLng(1, 1)));
    assertEquals(iconMap.size(), 2);
    assertTrue(iconMap.containsKey(icon));
    assertTrue(iconMap.get(icon) == 1);
  }

  @Test
  @UiThreadTest
  public void testAddRemoveIconMarker() {
    validateTestSetup();
    Icon icon = IconFactory.getInstance(rule.getActivity()).fromResource(R.drawable.mapvina_compass_icon);
    Marker marker = mapvinaMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng()));
    mapvinaMap.addMarker(new MarkerOptions().position(new LatLng(1, 1)));
    assertEquals(iconMap.size(), 2);
    assertTrue(iconMap.containsKey(icon));
    assertTrue(iconMap.get(icon) == 1);

    mapvinaMap.removeMarker(marker);
    assertEquals(iconMap.size(), 1);
    assertFalse(iconMap.containsKey(icon));
  }

  @Test
  @UiThreadTest
  public void testAddRemoveDefaultMarker() {
    validateTestSetup();
    Marker marker = mapvinaMap.addMarker(new MarkerOptions().position(new LatLng(1, 1)));
    assertEquals(iconMap.size(), 1);

    mapvinaMap.removeMarker(marker);
    assertEquals(iconMap.size(), 0);

    mapvinaMap.addMarker(new MarkerOptions().position(new LatLng()));
    assertEquals(iconMap.size(), 1);
  }

  @Test
  @UiThreadTest
  public void testAddRemoveMany() {
    validateTestSetup();
    Activity activity = rule.getActivity();
    IconFactory iconFactory = IconFactory.getInstance(activity);

    // add 2 default icon markers
    Marker defaultMarkerOne = mapvinaMap.addMarker(new MarkerOptions().position(new LatLng(1, 1)));
    Marker defaultMarkerTwo = mapvinaMap.addMarker(new MarkerOptions().position(new LatLng(2, 1)));

    // add 4 unique icon markers
    mapvinaMap.addMarker(new MarkerOptions()
      .icon(iconFactory.fromResource(R.drawable.mapvina_mylocation_icon_default))
      .position(new LatLng(3, 1))
    );
    mapvinaMap.addMarker(new MarkerOptions()
      .icon(iconFactory.fromResource(R.drawable.mapvina_compass_icon))
      .position(new LatLng(4, 1))
    );
    mapvinaMap.addMarker(new MarkerOptions()
      .icon(IconUtils.drawableToIcon(activity, R.drawable.ic_stars,
        ResourcesCompat.getColor(activity.getResources(),
          R.color.blueAccent, activity.getTheme())))
      .position(new LatLng(5, 1))
    );
    mapvinaMap.addMarker(new MarkerOptions()
      .icon(iconFactory.fromResource(R.drawable.ic_android))
      .position(new LatLng(6, 1))
    );

    assertEquals("Amount of icons should match 5", 5, iconMap.size());
    assertEquals("Refcounter of default marker should match 2", 2, iconMap.get(iconFactory.defaultMarker()), 0);

    mapvinaMap.removeMarker(defaultMarkerOne);

    assertEquals("Amount of icons should match 5", 5, iconMap.size());
    assertEquals("Refcounter of default marker should match 1", 1, iconMap.get(iconFactory.defaultMarker()), 0);

    mapvinaMap.removeMarker(defaultMarkerTwo);

    assertEquals("Amount of icons should match 4", 4, iconMap.size());
    assertNull("DefaultMarker shouldn't exist anymore", iconMap.get(iconFactory.defaultMarker()));

    mapvinaMap.clear();
    assertEquals("Amount of icons should match 0", 0, iconMap.size());
  }
}
