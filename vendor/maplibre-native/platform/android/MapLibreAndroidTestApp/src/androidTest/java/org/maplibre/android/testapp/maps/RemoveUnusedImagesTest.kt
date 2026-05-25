package com.mapvina.android.testapp.maps

import android.graphics.Bitmap
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.mapvina.android.AppCenter
import com.mapvina.android.camera.CameraUpdateFactory
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.activity.espresso.EspressoTestActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4ClassRunner::class)
class RemoveUnusedImagesTest : AppCenter() {

    @Rule
    @JvmField
    var rule = ActivityTestRule(EspressoTestActivity::class.java)

    private lateinit var mapView: MapView
    private lateinit var mapvinaMap: MapVinaMap
    private val latch = CountDownLatch(1)

    @Before
    fun setup() {
        rule.runOnUiThread {
            mapView = rule.activity.findViewById(R.id.mapView)
            mapView.getMapAsync {
                mapvinaMap = it
                mapvinaMap.setStyle(Style.Builder().fromJson(styleJson))
            }
        }
    }

    @Test
    fun testRemoveUnusedImagesUserProvidedListener() {
        var callbackLatch = CountDownLatch(2)
        rule.runOnUiThread {
            mapView.addOnStyleImageMissingListener {
                mapvinaMap.style!!.addImage(it, Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888))
            }

            // Remove layer and source, so that rendered tiles are no longer used, therefore, map must
            // notify client about unused images.
            mapView.addOnDidBecomeIdleListener {
                mapvinaMap.style!!.removeLayer("icon")
                mapvinaMap.style!!.removeSource("geojson")
            }

            mapView.addOnCanRemoveUnusedStyleImageListener {
                callbackLatch.countDown()
                mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 120.0), 8.0))
                mapView.addOnDidFinishRenderingFrameListener{ _, _ ->
                    assertNotNull(mapvinaMap.style!!.getImage("small"))
                    assertNotNull(mapvinaMap.style!!.getImage("large"))
                    latch.countDown()
                }
                return@addOnCanRemoveUnusedStyleImageListener false
            }
        }

        if (!latch.await(5, TimeUnit.SECONDS) && !callbackLatch.await(5, TimeUnit.SECONDS)) {
            throw TimeoutException()
        }
    }

    @Test
    fun testRemoveUnusedImagesDefaultListener() {
        rule.runOnUiThread {
            mapView.addOnStyleImageMissingListener {
                mapvinaMap.style!!.addImage(it, Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888))
            }

            // Remove layer and source, so that rendered tiles are no longer used, thus
            // map must request removal of unused images.
            mapView.addOnDidBecomeIdleListener {
                mapvinaMap.style!!.removeLayer("icon")
                mapvinaMap.style!!.removeSource("geojson")
                mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 120.0), 8.0))

                // Wait for the next frame and check that images were removed from the style.
                mapView.addOnDidFinishRenderingFrameListener { _, _ ->
                    if (mapvinaMap.style!!.getImage("small") == null && mapvinaMap.style!!.getImage("large") == null) {
                        latch.countDown()
                    }
                }
            }
        }

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw TimeoutException()
        }
    }

    companion object {
        private const val styleJson =
            """
    {
      "version": 8,
      "name": "MapVina Streets",
      "sources": {
        "geojson": {
          "type": "geojson",
          "data": {
            "type": "FeatureCollection",
            "features": [
              {
                "type": "Feature",
                "properties": {
                  "image": "small"
                },
                "geometry": {
                  "type": "Point",
                  "coordinates": [
                    0,
                    0
                  ]
                }
              },
              {
                "type": "Feature",
                "properties": {
                  "image": "large"
                },
                "geometry": {
                  "type": "Point",
                  "coordinates": [
                    1,
                    1
                  ]
                }
              }
            ]
          }
        }
      },
      "layers": [{
        "id": "bg",
        "type": "background",
        "paint": {
          "background-color": "#f00"
        }
      },{
        "id": "icon",
        "type": "symbol",
        "source": "geojson",
        "layout": {
          "icon-image": ["get", "image"]
        }
      }]
    }
    """
    }
}
