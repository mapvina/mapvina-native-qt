package com.mapvina.android.testapp.model.annotations

import com.mapvina.android.annotations.Marker

class CityStateMarker(
    cityStateOptions: CityStateMarkerOptions?,
    val infoWindowBackgroundColor: String
) : Marker(cityStateOptions)
