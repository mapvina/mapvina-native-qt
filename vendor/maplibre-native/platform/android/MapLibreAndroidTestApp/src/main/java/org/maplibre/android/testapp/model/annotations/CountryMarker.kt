package com.mapvina.android.testapp.model.annotations

import com.mapvina.android.annotations.BaseMarkerOptions
import com.mapvina.android.annotations.Marker

class CountryMarker(
    baseMarkerOptions: BaseMarkerOptions<*, *>?,
    val abbrevName: String,
    val flagRes: Int
) : Marker(baseMarkerOptions)
