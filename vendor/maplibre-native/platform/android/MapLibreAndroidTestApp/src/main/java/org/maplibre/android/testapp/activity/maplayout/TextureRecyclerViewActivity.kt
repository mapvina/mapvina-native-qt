package com.mapvina.android.testapp.activity.maplayout

import android.annotation.SuppressLint
import com.mapvina.android.testapp.R

/**
 * TestActivity showcasing how to integrate multiple TexureView MapViews in a RecyclerView.
 */
@SuppressLint("ClickableViewAccessibility")
class TextureRecyclerViewActivity : SurfaceRecyclerViewActivity() {

    override fun getMapItemLayoutId(): Int {
        return R.layout.item_map_texture
    }
}
