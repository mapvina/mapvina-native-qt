package com.mapvina.android.testapp.activity.maplayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.android.camera.CameraUpdateFactory
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.maps.*
import com.mapvina.android.maps.MapView.OnCameraDidChangeListener
import com.mapvina.android.maps.MapView.OnCameraIsChangingListener
import com.mapvina.android.maps.MapView.OnCameraWillChangeListener
import com.mapvina.android.maps.MapView.OnDidBecomeIdleListener
import com.mapvina.android.maps.MapView.OnDidFailLoadingMapListener
import com.mapvina.android.maps.MapView.OnDidFinishLoadingMapListener
import com.mapvina.android.maps.MapView.OnDidFinishLoadingStyleListener
import com.mapvina.android.maps.MapView.OnDidFinishRenderingFrameListener
import com.mapvina.android.maps.MapView.OnDidFinishRenderingMapListener
import com.mapvina.android.maps.MapView.OnSourceChangedListener
import com.mapvina.android.maps.MapView.OnWillStartLoadingMapListener
import com.mapvina.android.maps.MapView.OnWillStartRenderingFrameListener
import com.mapvina.android.maps.MapView.OnWillStartRenderingMapListener
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles
import timber.log.Timber

/**
 * Test activity showcasing how to listen to map change events.
 */
class MapChangeActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_simple)
        mapView = findViewById(R.id.mapView)
        mapView.addOnCameraIsChangingListener(OnCameraIsChangingListener { Timber.v("OnCameraIsChanging") })
        mapView.addOnCameraDidChangeListener(
            OnCameraDidChangeListener { animated: Boolean ->
                Timber.v(
                    "OnCamaraDidChange: animated: %s",
                    animated
                )
            }
        )
        mapView.addOnCameraWillChangeListener(
            OnCameraWillChangeListener { animated: Boolean ->
                Timber.v(
                    "OnCameraWilChange: animated: %s",
                    animated
                )
            }
        )
        mapView.addOnDidFailLoadingMapListener(
            OnDidFailLoadingMapListener { errorMessage: String? ->
                Timber.v(
                    "OnDidFailLoadingMap: %s",
                    errorMessage
                )
            }
        )
        mapView.addOnDidFinishLoadingMapListener(OnDidFinishLoadingMapListener { Timber.v("OnDidFinishLoadingMap") })
        mapView.addOnDidFinishLoadingStyleListener(OnDidFinishLoadingStyleListener { Timber.v("OnDidFinishLoadingStyle") })
        mapView.addOnDidFinishRenderingFrameListener(
            OnDidFinishRenderingFrameListener { fully: Boolean, _: Double, _: Double ->
                Timber.v(
                    "OnDidFinishRenderingFrame: fully: %s",
                    fully
                )
            }
        )
        mapView.addOnDidFinishRenderingMapListener(
            OnDidFinishRenderingMapListener { fully: Boolean ->
                Timber.v(
                    "OnDidFinishRenderingMap: fully: %s",
                    fully
                )
            }
        )
        mapView.addOnDidBecomeIdleListener(OnDidBecomeIdleListener { Timber.v("OnDidBecomeIdle") })
        mapView.addOnSourceChangedListener(
            OnSourceChangedListener { sourceId: String? ->
                Timber.v(
                    "OnSourceChangedListener: source with id: %s",
                    sourceId
                )
            }
        )
        mapView.addOnWillStartLoadingMapListener(OnWillStartLoadingMapListener { Timber.v("OnWillStartLoadingMap") })
        mapView.addOnWillStartRenderingFrameListener(OnWillStartRenderingFrameListener { Timber.v("OnWillStartRenderingFrame") })
        mapView.addOnWillStartRenderingMapListener(OnWillStartRenderingMapListener { Timber.v("OnWillStartRenderingMap") })
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(
            OnMapReadyCallback { mapvinaMap: MapVinaMap ->
                mapvinaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets"))
                mapvinaMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(55.754020, 37.620948),
                        12.0
                    ),
                    9000
                )
            }
        )
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
