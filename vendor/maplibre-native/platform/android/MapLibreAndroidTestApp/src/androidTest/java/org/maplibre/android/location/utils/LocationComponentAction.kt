package com.mapvina.android.location.utils

import android.content.Context
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.mapvina.android.location.LocationComponent
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import org.hamcrest.Matcher

class LocationComponentAction(
    private val mapvinaMap: MapVinaMap,
    private val onPerformLocationComponentAction: OnPerformLocationComponentAction
) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return isDisplayed()
    }

    override fun getDescription(): String {
        return javaClass.simpleName
    }

    override fun perform(uiController: UiController, view: View) {
        onPerformLocationComponentAction.onLocationComponentAction(
            mapvinaMap.locationComponent,
            mapvinaMap,
            mapvinaMap.style!!,
            uiController,
            view.context
        )
    }

    interface OnPerformLocationComponentAction {
        fun onLocationComponentAction(component: LocationComponent, mapvinaMap: MapVinaMap, style: Style, uiController: UiController, context: Context)
    }
}
