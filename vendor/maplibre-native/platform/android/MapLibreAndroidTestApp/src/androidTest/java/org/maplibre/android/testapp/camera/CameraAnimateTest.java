package com.mapvina.android.testapp.camera;

import com.mapvina.android.camera.CameraUpdate;
import com.mapvina.android.maps.MapVinaMap;

public class CameraAnimateTest extends CameraTest {
  @Override
  void executeCameraMovement(CameraUpdate cameraUpdate, MapVinaMap.CancelableCallback callback) {
    mapvinaMap.animateCamera(cameraUpdate, callback);
  }
}
