package com.mapvina.android.maps

import android.content.Context
import android.graphics.PointF
import com.mapvina.android.MapVinaInjector
import com.mapvina.android.camera.CameraPosition
import com.mapvina.android.camera.CameraUpdateFactory
import com.mapvina.android.constants.MapVinaConstants
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.geometry.LatLngBounds
import com.mapvina.android.style.layers.TransitionOptions
import com.mapvina.android.utils.ConfigUtils
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MapVinaMapTest {

    private lateinit var mapvinaMap: MapVinaMap

    private lateinit var nativeMapView: NativeMap

    private lateinit var transform: Transform

    private lateinit var cameraChangeDispatcher: CameraChangeDispatcher

    private lateinit var developerAnimationListener: MapVinaMap.OnDeveloperAnimationListener

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var appContext: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        MapVinaInjector.inject(context, "abcdef", ConfigUtils.getMockedOptions())
        cameraChangeDispatcher = spyk()
        developerAnimationListener = mockk(relaxed = true)
        nativeMapView = mockk(relaxed = true)
        transform = mockk(relaxed = true)
        mapvinaMap = MapVinaMap(
            nativeMapView,
            transform,
            mockk(relaxed = true),
            null,
            null,
            cameraChangeDispatcher,
            listOf(developerAnimationListener)
        )
        every { nativeMapView.isDestroyed } returns false
        every { nativeMapView.nativePtr } returns 5
        mapvinaMap.injectLocationComponent(spyk())
        mapvinaMap.setStyle(Style.getPredefinedStyle("Streets"))
        mapvinaMap.onFinishLoadingStyle()
    }

    @Test
    fun testTransitionOptions() {
        val expected = TransitionOptions(100, 200)
        mapvinaMap.style?.transition = expected
        verify { nativeMapView.transitionOptions = expected }
    }

    @Test
    fun testMoveCamera() {
        val callback = mockk<MapVinaMap.CancelableCallback>()
        val target = LatLng(1.0, 2.0)
        val expected = CameraPosition.Builder().target(target).build()
        val update = CameraUpdateFactory.newCameraPosition(expected)
        mapvinaMap.moveCamera(update, callback)
        verify { transform.moveCamera(mapvinaMap, update, callback) }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testEaseCamera() {
        val callback = mockk<MapVinaMap.CancelableCallback>()
        val target = LatLng(1.0, 2.0)
        val expected = CameraPosition.Builder().target(target).build()
        val update = CameraUpdateFactory.newCameraPosition(expected)
        mapvinaMap.easeCamera(update, callback)
        verify { transform.easeCamera(mapvinaMap, update, MapVinaConstants.ANIMATION_DURATION, true, callback) }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testAnimateCamera() {
        val callback = mockk<MapVinaMap.CancelableCallback>()
        val target = LatLng(1.0, 2.0)
        val expected = CameraPosition.Builder().target(target).build()
        val update = CameraUpdateFactory.newCameraPosition(expected)
        mapvinaMap.animateCamera(update, callback)
        verify { transform.animateCamera(mapvinaMap, update, MapVinaConstants.ANIMATION_DURATION, callback) }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testScrollBy() {
        mapvinaMap.scrollBy(100f, 200f)
        verify { nativeMapView.moveBy(100.0, 200.0, 0) }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testResetNorth() {
        mapvinaMap.resetNorth()
        verify { transform.resetNorth() }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testFocalBearing() {
        mapvinaMap.setFocalBearing(35.0, 100f, 200f, 1000)
        verify { transform.setBearing(35.0, 100f, 200f, 1000) }
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
    }

    @Test
    fun testMinZoom() {
        mapvinaMap.setMinZoomPreference(10.0)
        verify { transform.minZoom = 10.0 }
    }

    @Test
    fun testMaxZoom() {
        mapvinaMap.setMaxZoomPreference(10.0)
        verify { transform.maxZoom = 10.0 }
    }

    @Test
    fun testMinPitch() {
        mapvinaMap.setMinPitchPreference(10.0)
        verify { transform.minPitch = 10.0 }
    }

    @Test
    fun testMaxPitch() {
        mapvinaMap.setMaxPitchPreference(10.0)
        verify { transform.maxPitch = 10.0 }
    }

    @Test
    fun testFpsListener() {
        val fpsChangedListener = mockk<MapVinaMap.OnFpsChangedListener>()
        mapvinaMap.onFpsChangedListener = fpsChangedListener
        assertEquals("Listener should match", fpsChangedListener, mapvinaMap.onFpsChangedListener)
    }

    @Test
    fun testTilePrefetch() {
        mapvinaMap.prefetchesTiles = true
        verify { nativeMapView.prefetchTiles = true }
    }

    @Test
    fun testGetPrefetchZoomDelta() {
        every { nativeMapView.prefetchZoomDelta } answers { 3 }
        assertEquals(3, mapvinaMap.prefetchZoomDelta)
    }

    @Test
    fun testSetPrefetchZoomDelta() {
        mapvinaMap.prefetchZoomDelta = 2
        verify { nativeMapView.prefetchZoomDelta = 2 }
    }

    @Test
    fun testCameraForLatLngBounds() {
        val bounds = LatLngBounds.Builder().include(LatLng()).include(LatLng(1.0, 1.0)).build()
        mapvinaMap.setLatLngBoundsForCameraTarget(bounds)
        verify { nativeMapView.setLatLngBounds(bounds) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAnimateCameraChecksDurationPositive() {
        mapvinaMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(30.0, 30.0)), 0, null)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEaseCameraChecksDurationPositive() {
        mapvinaMap.easeCamera(CameraUpdateFactory.newLatLng(LatLng(30.0, 30.0)), 0, null)
    }

    @Test
    fun testGetNativeMapPtr() {
        assertEquals(5, mapvinaMap.nativeMapPtr)
    }

    @Test
    fun testNativeMapIsNotCalledOnStateSave() {
        clearMocks(nativeMapView)
        mapvinaMap.onSaveInstanceState(mockk(relaxed = true))
        verify { nativeMapView wasNot Called }
    }

    @Test
    fun testCameraChangeDispatcherCleared() {
        mapvinaMap.onDestroy()
        verify { cameraChangeDispatcher.onDestroy() }
    }

    @Test
    fun testStyleClearedOnDestroy() {
        val style = mockk<Style>(relaxed = true)
        val builder = mockk<Style.Builder>(relaxed = true)
        every { builder.build(nativeMapView) } returns style
        mapvinaMap.setStyle(builder)

        mapvinaMap.onDestroy()
        verify(exactly = 1) { style.clear() }
    }

    @Test
    fun testStyleCallbackNotCalledWhenPreviousFailed() {
        val style = mockk<Style>(relaxed = true)
        val builder = mockk<Style.Builder>(relaxed = true)
        every { builder.build(nativeMapView) } returns style
        val onStyleLoadedListener = mockk<Style.OnStyleLoaded>(relaxed = true)

        mapvinaMap.setStyle(builder, onStyleLoadedListener)
        mapvinaMap.onFailLoadingStyle()
        mapvinaMap.setStyle(builder, onStyleLoadedListener)
        mapvinaMap.onFinishLoadingStyle()
        verify(exactly = 1) { onStyleLoadedListener.onStyleLoaded(style) }
    }

    @Test
    fun testStyleCallbackNotCalledWhenPreviousNotFinished() {
        // regression test for #14337
        val style = mockk<Style>(relaxed = true)
        val builder = mockk<Style.Builder>(relaxed = true)
        every { builder.build(nativeMapView) } returns style
        val onStyleLoadedListener = mockk<Style.OnStyleLoaded>(relaxed = true)

        mapvinaMap.setStyle(builder, onStyleLoadedListener)
        mapvinaMap.setStyle(builder, onStyleLoadedListener)
        mapvinaMap.onFinishLoadingStyle()
        verify(exactly = 1) { onStyleLoadedListener.onStyleLoaded(style) }
    }

    @Test
    fun testGetZoom() {
        mapvinaMap.zoom
        verify { nativeMapView.zoom }
        assertEquals(mapvinaMap.zoom, 0.0, 0.0)
    }

    @Test
    fun testSetZoom() {
        val target = PointF(100f, 100f)
        mapvinaMap.setZoom(2.0, target, 0)
        verify { developerAnimationListener.onDeveloperAnimationStarted() }
        verify { nativeMapView.setZoom(2.0, target, 0) }
    }
}
