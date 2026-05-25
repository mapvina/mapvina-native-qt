package com.mapvina.android.maps

import android.graphics.PointF
import androidx.test.espresso.UiController
import com.mapvina.android.camera.CameraUpdateFactory
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.geometry.LatLngBounds
import com.mapvina.android.testapp.action.MapVinaMapAction.invoke
import com.mapvina.android.testapp.activity.BaseTest
import com.mapvina.android.testapp.activity.espresso.PixelTestActivity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test

class VisibleRegionTest : BaseTest() {

    override fun getActivityClass(): Class<*> {
        return PixelTestActivity::class.java
    }

    override
    fun beforeTest() {
        super.beforeTest()
        mapView = (rule.activity as PixelTestActivity).mapView
    }

    @Test
    fun visibleRegionTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )
            val visibleRegion = mapvinaMap.projection.visibleRegion
            assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
        }
    }

    @Test
    fun paddedVisibleRegionTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapvinaMap.setPadding(
                mapView.width / 4,
                mapView.height / 4,
                mapView.width / 4,
                mapView.height / 4
            )

            val visibleRegion = mapvinaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 1)
            assertTrue(filtered.contains(mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedLeftVisibleRegionTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapvinaMap.setPadding(mapView.width / 4, 0, 0, 0)

            val visibleRegion = mapvinaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedTopVisibleRegionTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapvinaMap.setPadding(0, mapView.height / 4, 0, 0)

            val visibleRegion = mapvinaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f)))
        }
    }

    @Test
    fun paddedRightVisibleRegionTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapvinaMap.setPadding(0, 0, mapView.width / 4, 0)

            val visibleRegion = mapvinaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)))
        }
    }

    @Test
    fun paddedBottomVisibleRegionTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapvinaMap.setPadding(0, 0, 0, mapView.height / 4)

            val visibleRegion = mapvinaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat())))
        }
    }

    @Test
    fun visibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )
            val visibleRegion = mapvinaMap.projection.visibleRegion
            assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
        }
    }

    @Test
    fun paddedVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapvinaMap.setPadding(
                mapView.width / 4,
                mapView.height / 4,
                mapView.width / 4,
                mapView.height / 4
            )

            val visibleRegion = mapvinaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 1)
            assertTrue(filtered.contains(mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedLeftVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapvinaMap.setPadding(mapView.width / 4, 0, 0, 0)

            val visibleRegion = mapvinaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedTopVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapvinaMap) { ui: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            ui.loopMainThreadForAtLeast(5000)
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapvinaMap.setPadding(0, mapView.height / 4, 0, 0)

            val visibleRegion = mapvinaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f)))
        }
    }

    @Test
    fun paddedRightVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapvinaMap.setPadding(0, 0, mapView.width / 4, 0)

            val visibleRegion = mapvinaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)))
        }
    }

    @Test
    fun paddedBottomVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapvinaMap.setPadding(0, 0, 0, mapView.height / 4)

            val visibleRegion = mapvinaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat())))
        }
    }

    @Test
    fun visibleRotatedRegionTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val d = Math.min(mapvinaMap.width, mapvinaMap.height) / 4
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f)
            )

            for (bearing in 45 until 360 step 45) {
                mapvinaMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val visibleRegion = mapvinaMap.projection.visibleRegion
                assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
            }
        }
    }

    @Test
    fun visibleRotatedRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val d = Math.min(mapvinaMap.width, mapvinaMap.height) / 4
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f)
            )

            for (bearing in 45 until 360 step 45) {
                mapvinaMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val visibleRegion = mapvinaMap.projection.visibleRegion
                assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
            }
        }
    }

    @Test
    fun visibleRegionWithBoundsEqualTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )
            val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
            mapvinaMap.projection.getVisibleCoordinateBounds(bounds)
            val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
            val visibleRegion = mapvinaMap.projection.visibleRegion
            assertTrue(latLngBounds == visibleRegion.latLngBounds)
            assertTrue(latLngs.all { latLngBounds.contains(it) })
        }
    }

    @Test
    fun visibleRegionBoundsOverDatelineTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(0f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapvinaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )
            val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
            mapvinaMap.projection.getVisibleCoordinateBounds(bounds)
            val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
            assertTrue(latLngs.all { latLngBounds.contains(it) })
        }
    }

    @Test
    fun visibleRegionBoundsOverDatelineLatitudeZeroTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val shift = mapvinaMap.getLatLngFromScreenCoords(0f, 0f)
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.0, 180.0 - shift.longitude)))

            val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
            mapvinaMap.projection.getVisibleCoordinateBounds(bounds)
            val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
            val visibleRegion = mapvinaMap.projection.visibleRegion
            assertTrue(latLngBounds == visibleRegion.latLngBounds)
        }
    }

    @Test
    fun visibleRotatedRegionBoundEqualTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val d = Math.min(mapvinaMap.width, mapvinaMap.height) / 4
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f)
            )

            for (bearing in 45 until 360 step 45) {
                mapvinaMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
                mapvinaMap.projection.getVisibleCoordinateBounds(bounds)
                val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
                val visibleRegion = mapvinaMap.projection.visibleRegion
                assertTrue(latLngBounds == visibleRegion.latLngBounds)
                assertTrue(latLngs.all { latLngBounds.contains(it) })
            }
        }
    }

    @Test
    fun visibleRotatedRegionBoundsOverDatelineTest() {
        validateTestSetup()
        invoke(mapvinaMap) { _: UiController, mapvinaMap: MapVinaMap ->
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 179.0), 8.0))
            val d = Math.min(mapvinaMap.width, mapvinaMap.height) / 4
            val latLngs = listOf(
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                mapvinaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f)
            )

            for (bearing in 45 until 360 step 45) {
                mapvinaMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
                mapvinaMap.projection.getVisibleCoordinateBounds(bounds)
                val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
                assertTrue(latLngs.all { latLngBounds.contains(it) })
            }
        }
    }

    private fun MapVinaMap.getLatLngFromScreenCoords(x: Float, y: Float): LatLng {
        return this.projection.fromScreenLocation(PointF(x, y))
    }
}
