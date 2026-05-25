package com.mapvina.android.testapp.utils

import android.widget.TextView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.MapVinaMap.OnCameraIdleListener
import com.mapvina.android.testapp.R

class IdleZoomListener(private val mapvinaMap: MapVinaMap, private val textView: TextView) :
    OnCameraIdleListener {
    override fun onCameraIdle() {
        val context = textView.context
        val position = mapvinaMap.cameraPosition
        textView.text = String.format(context.getString(R.string.debug_zoom), position.zoom)
    }
}
