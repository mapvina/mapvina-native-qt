package com.mapvina.android.testapp.activity.render

import android.content.Context
import com.mapvina.android.snapshotter.MapSnapshot
import com.mapvina.android.snapshotter.MapSnapshotter

class RenderTestSnapshotter internal constructor(context: Context, options: Options) :
    MapSnapshotter(context, options) {
    override fun addOverlay(mapSnapshot: MapSnapshot) {
        // don't add an overlay
    }
}
