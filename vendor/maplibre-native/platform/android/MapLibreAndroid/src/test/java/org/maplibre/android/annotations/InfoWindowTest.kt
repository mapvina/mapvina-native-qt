package com.mapvina.android.annotations

import android.graphics.PointF
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Projection
import org.junit.Assert
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito

class InfoWindowTest {
    @InjectMocks
    var mMapView = Mockito.mock(MapView::class.java)

    @InjectMocks
    var mMapVinaMap = Mockito.mock(MapVinaMap::class.java)

    @Test
    fun testSanity() {
        val infoWindow = InfoWindow(mMapView, mMapVinaMap)
        Assert.assertNotNull("infoWindow should exist", infoWindow)
    }

    @Test
    fun testBoundMarker() {
        val markerOptions = MarkerOptions()
        val marker = markerOptions.position(LatLng()).marker
        val infoWindow = InfoWindow(mMapView, mMapVinaMap).setBoundMarker(marker)
        Assert.assertEquals("marker should match", marker, infoWindow.boundMarker)
    }

    @Test
    fun testClose() {
        val infoWindow = InfoWindow(mMapView, mMapVinaMap)
        infoWindow.close()
        Assert.assertEquals("infowindow should not be visible", false, infoWindow.isVisible)
    }

    @Test
    fun testOpen() {
        val latLng = LatLng(0.0, 0.0)
        val projection = Mockito.mock(
            Projection::class.java
        )
        Mockito.`when`(mMapVinaMap.projection).thenReturn(projection)
        Mockito.`when`(projection.toScreenLocation(latLng)).thenReturn(PointF(0f, 0f))
        val infoWindow = InfoWindow(mMapView, mMapVinaMap)
        infoWindow.open(mMapView, MarkerOptions().position(LatLng()).marker, latLng, 0, 0)
        Assert.assertEquals("infowindow should not be visible", true, infoWindow.isVisible)
    }

    @Test
    fun testOpenClose() {
        val latLng = LatLng(0.0, 0.0)
        val projection = Mockito.mock(
            Projection::class.java
        )
        Mockito.`when`(mMapVinaMap.projection).thenReturn(projection)
        Mockito.`when`(projection.toScreenLocation(latLng)).thenReturn(PointF(0f, 0f))
        val infoWindow = InfoWindow(mMapView, mMapVinaMap)
        infoWindow.open(mMapView, MarkerOptions().position(LatLng()).marker, latLng, 0, 0)
        infoWindow.close()
        Assert.assertEquals("infowindow should not be visible", false, infoWindow.isVisible)
    }

    @Test
    fun testUpdate() {
        val latLng = LatLng(0.0, 0.0)
        val projection = Mockito.mock(
            Projection::class.java
        )
        Mockito.`when`(mMapVinaMap.projection).thenReturn(projection)
        Mockito.`when`(projection.toScreenLocation(latLng)).thenReturn(PointF(0f, 0f))
        val infoWindow = InfoWindow(mMapView, mMapVinaMap)
        infoWindow.open(mMapView, MarkerOptions().position(latLng).marker, latLng, 0, 0)
        infoWindow.update()
    }
}
