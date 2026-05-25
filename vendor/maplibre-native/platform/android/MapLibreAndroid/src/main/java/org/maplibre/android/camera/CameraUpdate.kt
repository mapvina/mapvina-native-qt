package com.mapvina.android.camera

import com.mapvina.android.maps.MapVinaMap

/**
 * Interface definition for camera updates.
 */
interface CameraUpdate {
    /**
     * Get the camera position from the camera update.
     *
     * @param mapvinaMap Map object to build the position from
     * @return the camera position from the implementing camera update
     */
    fun getCameraPosition(mapvinaMap: MapVinaMap): CameraPosition?
}
