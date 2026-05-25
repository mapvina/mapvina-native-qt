package com.mapvina.android.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.ShapeDrawable
import com.mapvina.android.MapVinaInjector
import com.mapvina.android.constants.MapVinaConstants
import com.mapvina.android.style.layers.CannotAddLayerException
import com.mapvina.android.style.layers.SymbolLayer
import com.mapvina.android.style.layers.TransitionOptions
import com.mapvina.android.style.sources.CannotAddSourceException
import com.mapvina.android.style.sources.GeoJsonSource
import com.mapvina.android.utils.ConfigUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StyleTest {

    private lateinit var mapvinaMap: MapVinaMap

    private lateinit var nativeMapView: NativeMap

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var appContext: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        MapVinaInjector.inject(context, "abcdef", ConfigUtils.getMockedOptions())
        nativeMapView = mockk(relaxed = true)
        mapvinaMap = MapVinaMap(
            nativeMapView,
            null,
            null,
            null,
            null,
            null,
            null
        )
        every { nativeMapView.isDestroyed } returns false
        mapvinaMap.injectLocationComponent(spyk())
    }

    @Test
    fun testFromUrl() {
        val builder = Style.Builder().fromUri(Style.getPredefinedStyle("Streets"))
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Streets") }
    }

    @Test
    fun testFromJson() {
        val builder = Style.Builder().fromJson("{}")
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleJson = "{}" }
    }

    @Test
    fun testEmptyBuilder() {
        val builder = Style.Builder()
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleJson = Style.EMPTY_JSON }
    }

    @Test
    fun testWithLayer() {
        val layer = mockk<SymbolLayer>()
        every { layer.id } returns "1"
        val builder = Style.Builder().withLayer(layer)
        mapvinaMap.setStyle(builder)
        mapvinaMap.onFinishLoadingStyle()
        verify(exactly = 1) {
            nativeMapView.addLayerBelow(
                layer,
                MapVinaConstants.LAYER_ID_ANNOTATIONS
            )
        }
    }

    @Test
    fun testWithLayerAbove() {
        val layer = mockk<SymbolLayer>()
        every { layer.id } returns "1"
        val builder = Style.Builder().withLayerAbove(layer, "id")
        mapvinaMap.setStyle(builder)
        mapvinaMap.onFinishLoadingStyle()
        verify(exactly = 1) { nativeMapView.addLayerAbove(layer, "id") }
    }

    @Test
    fun testWithLayerBelow() {
        val layer = mockk<SymbolLayer>()
        every { layer.id } returns "1"
        val builder = Style.Builder().withLayerBelow(layer, "id")
        mapvinaMap.setStyle(builder)
        mapvinaMap.onFinishLoadingStyle()
        verify(exactly = 1) { nativeMapView.addLayerBelow(layer, "id") }
    }

    @Test
    fun testWithLayerAt() {
        val layer = mockk<SymbolLayer>()
        every { layer.id } returns "1"
        val builder = Style.Builder().withLayerAt(layer, 1)
        mapvinaMap.setStyle(builder)
        mapvinaMap.onFinishLoadingStyle()
        verify(exactly = 1) { nativeMapView.addLayerAt(layer, 1) }
    }

    @Test
    fun testWithSource() {
        val source = mockk<GeoJsonSource>()
        every { source.id } returns "1"
        val builder = Style.Builder().withSource(source)
        mapvinaMap.setStyle(builder)
        mapvinaMap.onFinishLoadingStyle()
        verify(exactly = 1) { nativeMapView.addSource(source) }
    }

    @Test
    fun testWithTransitionOptions() {
        val transitionOptions = TransitionOptions(100, 200)
        val builder = Style.Builder().withTransition(transitionOptions)
        mapvinaMap.setStyle(builder)
        mapvinaMap.onFinishLoadingStyle()
        verify(exactly = 1) { nativeMapView.transitionOptions = transitionOptions }
    }

    @Test
    fun testWithFromLoadingSource() {
        val source = mockk<GeoJsonSource>()
        every { source.id } returns "1"
        val builder =
            Style.Builder().fromUri(Style.getPredefinedStyle("Streets")).withSource(source)
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Streets") }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) { nativeMapView.addSource(source) }
    }

    @Test
    fun testWithFromLoadingLayer() {
        val layer = mockk<SymbolLayer>()
        every { layer.id } returns "1"
        val builder = Style.Builder().fromUri(Style.getPredefinedStyle("Streets")).withLayer(layer)
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Streets") }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) {
            nativeMapView.addLayerBelow(
                layer,
                MapVinaConstants.LAYER_ID_ANNOTATIONS
            )
        }
    }

    @Test
    fun testWithFromLoadingLayerAt() {
        val layer = mockk<SymbolLayer>()
        every { layer.id } returns "1"
        val builder =
            Style.Builder().fromUri(Style.getPredefinedStyle("Streets")).withLayerAt(layer, 1)
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Streets") }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) { nativeMapView.addLayerAt(layer, 1) }
    }

    @Test
    fun testWithFromLoadingLayerBelow() {
        val layer = mockk<SymbolLayer>()
        every { layer.id } returns "1"
        val builder = Style.Builder().fromUri(Style.getPredefinedStyle("Streets"))
            .withLayerBelow(layer, "below")
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Streets") }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) { nativeMapView.addLayerBelow(layer, "below") }
    }

    @Test
    fun testWithFromLoadingLayerAbove() {
        val layer = mockk<SymbolLayer>()
        every { layer.id } returns "1"
        val builder = Style.Builder().fromUri(Style.getPredefinedStyle("Streets"))
            .withLayerBelow(layer, "below")
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Streets") }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) { nativeMapView.addLayerBelow(layer, "below") }
    }

    @Test
    fun testWithFromLoadingTransitionOptions() {
        val transitionOptions = TransitionOptions(100, 200)
        val builder = Style.Builder().fromUri(Style.getPredefinedStyle("Streets"))
            .withTransition(transitionOptions)
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Streets") }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) { nativeMapView.transitionOptions = transitionOptions }
    }

    @Test
    fun testFromCallback() {
        val callback = mockk<Style.OnStyleLoaded>()
        every { callback.onStyleLoaded(any()) } answers {}
        val builder = Style.Builder().fromUri(Style.getPredefinedStyle("Streets"))
        mapvinaMap.setStyle(builder, callback)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Streets") }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) { callback.onStyleLoaded(any()) }
    }

    @Test
    fun testWithCallback() {
        val callback = mockk<Style.OnStyleLoaded>()
        every { callback.onStyleLoaded(any()) } answers {}
        val source = mockk<GeoJsonSource>()
        every { source.id } returns "1"
        val builder = Style.Builder().withSource(source)
        mapvinaMap.setStyle(builder, callback)
        mapvinaMap.onFinishLoadingStyle()
        verify(exactly = 1) { nativeMapView.addSource(source) }
        verify(exactly = 1) { callback.onStyleLoaded(any()) }
    }

    @Test
    fun testGetAsyncWith() {
        val callback = mockk<Style.OnStyleLoaded>()
        every { callback.onStyleLoaded(any()) } answers {}
        mapvinaMap.getStyle(callback)
        val source = mockk<GeoJsonSource>()
        every { source.id } returns "1"
        val builder = Style.Builder().withSource(source)
        mapvinaMap.setStyle(builder)
        mapvinaMap.onFinishLoadingStyle()
        verify(exactly = 1) { nativeMapView.addSource(source) }
        verify(exactly = 1) { callback.onStyleLoaded(any()) }
    }

    @Test
    fun testGetAsyncFrom() {
        val callback = mockk<Style.OnStyleLoaded>()
        every { callback.onStyleLoaded(any()) } answers {}
        mapvinaMap.getStyle(callback)
        val source = mockk<GeoJsonSource>()
        every { source.id } returns "1"
        val builder = Style.Builder().fromJson("{}")
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleJson = "{}" }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) { callback.onStyleLoaded(any()) }
    }

    @Test
    fun testGetAsyncWithFrom() {
        val callback = mockk<Style.OnStyleLoaded>()
        every { callback.onStyleLoaded(any()) } answers {}
        mapvinaMap.getStyle(callback)
        val source = mockk<GeoJsonSource>()
        every { source.id } returns "1"
        val builder =
            Style.Builder().fromUri(Style.getPredefinedStyle("Streets")).withSource(source)
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Streets") }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) { nativeMapView.addSource(source) }
        verify(exactly = 1) { callback.onStyleLoaded(any()) }
    }

    @Test
    fun testGetNullStyle() {
        Assert.assertNull(mapvinaMap.style)
    }

    @Test
    fun testGetNullWhileLoading() {
        val transitionOptions = TransitionOptions(100, 200)
        val builder = Style.Builder().fromUri(Style.getPredefinedStyle("Streets"))
            .withTransition(transitionOptions)
        mapvinaMap.setStyle(builder)
        Assert.assertNull(mapvinaMap.style)
        mapvinaMap.notifyStyleLoaded()
        Assert.assertNotNull(mapvinaMap.style)
    }

    @Test
    fun testNotReinvokeSameListener() {
        val callback = mockk<Style.OnStyleLoaded>()
        every { callback.onStyleLoaded(any()) } answers {}
        mapvinaMap.getStyle(callback)
        val source = mockk<GeoJsonSource>()
        every { source.id } returns "1"
        val builder = Style.Builder().fromJson("{}")
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleJson = "{}" }
        mapvinaMap.notifyStyleLoaded()
        mapvinaMap.setStyle(Style.getPredefinedStyle("Streets"))
        verify(exactly = 1) { callback.onStyleLoaded(any()) }
    }

    @Test(expected = IllegalStateException::class)
    fun testIllegalStateExceptionWithStyleReload() {
        val builder = Style.Builder().fromUri(Style.getPredefinedStyle("Streets"))
        mapvinaMap.setStyle(builder)
        mapvinaMap.notifyStyleLoaded()
        val style = mapvinaMap.style
        mapvinaMap.setStyle(Style.Builder().fromUri(Style.getPredefinedStyle("Bright")))
        style!!.addLayer(mockk<SymbolLayer>())
    }

    @Test
    fun testAddImage() {
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        val builder =
            Style.Builder().fromUri(Style.getPredefinedStyle("Satellite Hybrid")).withImage("id", bitmap)
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Satellite Hybrid") }
        verify(exactly = 0) { nativeMapView.addImages(any()) }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) { nativeMapView.addImages(any()) }
    }

    @Test
    fun testAddDrawable() {
        val drawable = ShapeDrawable()
        drawable.intrinsicHeight = 10
        drawable.intrinsicWidth = 10
        val builder =
            Style.Builder().fromUri(Style.getPredefinedStyle("Satellite Hybrid")).withImage("id", drawable)
        mapvinaMap.setStyle(builder)
        verify(exactly = 1) { nativeMapView.styleUri = Style.getPredefinedStyle("Satellite Hybrid") }
        verify(exactly = 0) { nativeMapView.addImages(any()) }
        mapvinaMap.notifyStyleLoaded()
        verify(exactly = 1) { nativeMapView.addImages(any()) }
    }

    @Test
    fun testSourceSkippedIfAdditionFails() {
        val source1 = mockk<GeoJsonSource>(relaxed = true)
        every { source1.id } returns "source1"
        val source2 = mockk<GeoJsonSource>(relaxed = true)
        every { source2.id } returns "source1" // same ID

        val builder = Style.Builder().withSource(source1)
        mapvinaMap.setStyle(builder)
        mapvinaMap.notifyStyleLoaded()

        every { nativeMapView.addSource(any()) } throws CannotAddSourceException("Duplicate ID")

        try {
            mapvinaMap.style!!.addSource(source2)
        } catch (ex: Exception) {
            Assert.assertEquals(
                "Source that failed to be added shouldn't be cached",
                source1,
                mapvinaMap.style!!.getSource("source1")
            )
        }
    }

    @Test
    fun testLayerSkippedIfAdditionFails() {
        val layer1 = mockk<SymbolLayer>(relaxed = true)
        every { layer1.id } returns "layer1"
        val layer2 = mockk<SymbolLayer>(relaxed = true)
        every { layer2.id } returns "layer1" // same ID

        val builder = Style.Builder().withLayer(layer1)
        mapvinaMap.setStyle(builder)
        mapvinaMap.notifyStyleLoaded()

        every { nativeMapView.addLayer(any()) } throws CannotAddLayerException("Duplicate ID")

        try {
            mapvinaMap.style!!.addLayer(layer2)
        } catch (ex: Exception) {
            Assert.assertEquals(
                "Layer that failed to be added shouldn't be cached",
                layer1,
                mapvinaMap.style!!.getLayer("layer1")
            )
        }
    }

    @Test
    fun testLayerSkippedIfAdditionBelowFails() {
        val layer1 = mockk<SymbolLayer>(relaxed = true)
        every { layer1.id } returns "layer1"
        val layer2 = mockk<SymbolLayer>(relaxed = true)
        every { layer2.id } returns "layer1" // same ID

        val builder = Style.Builder().withLayer(layer1)
        mapvinaMap.setStyle(builder)
        mapvinaMap.notifyStyleLoaded()

        every {
            nativeMapView.addLayerBelow(
                any(),
                ""
            )
        } throws CannotAddLayerException("Duplicate ID")

        try {
            mapvinaMap.style!!.addLayerBelow(layer2, "")
        } catch (ex: Exception) {
            Assert.assertEquals(
                "Layer that failed to be added shouldn't be cached",
                layer1,
                mapvinaMap.style!!.getLayer("layer1")
            )
        }
    }

    @Test
    fun testLayerSkippedIfAdditionAboveFails() {
        val layer1 = mockk<SymbolLayer>(relaxed = true)
        every { layer1.id } returns "layer1"
        val layer2 = mockk<SymbolLayer>(relaxed = true)
        every { layer2.id } returns "layer1" // same ID

        val builder = Style.Builder().withLayer(layer1)
        mapvinaMap.setStyle(builder)
        mapvinaMap.notifyStyleLoaded()

        every {
            nativeMapView.addLayerAbove(
                any(),
                ""
            )
        } throws CannotAddLayerException("Duplicate ID")

        try {
            mapvinaMap.style!!.addLayerAbove(layer2, "")
        } catch (ex: Exception) {
            Assert.assertEquals(
                "Layer that failed to be added shouldn't be cached",
                layer1,
                mapvinaMap.style!!.getLayer("layer1")
            )
        }
    }

    @Test
    fun testLayerSkippedIfAdditionAtFails() {
        val layer1 = mockk<SymbolLayer>(relaxed = true)
        every { layer1.id } returns "layer1"
        val layer2 = mockk<SymbolLayer>(relaxed = true)
        every { layer2.id } returns "layer1" // same ID

        val builder = Style.Builder().withLayer(layer1)
        mapvinaMap.setStyle(builder)
        mapvinaMap.notifyStyleLoaded()

        every { nativeMapView.addLayerAt(any(), 5) } throws CannotAddLayerException("Duplicate ID")

        try {
            mapvinaMap.style!!.addLayerAt(layer2, 5)
        } catch (ex: Exception) {
            Assert.assertEquals(
                "Layer that failed to be added shouldn't be cached",
                layer1,
                mapvinaMap.style!!.getLayer("layer1")
            )
        }
    }
}
