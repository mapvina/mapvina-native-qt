package com.mapvina.android.maps.renderer.surfaceview;

import android.content.Context;
import androidx.annotation.NonNull;

import com.mapvina.android.maps.renderer.egl.EGLConfigChooser;
import com.mapvina.android.maps.renderer.egl.EGLContextFactory;
import com.mapvina.android.maps.renderer.egl.EGLWindowSurfaceFactory;
import com.mapvina.android.maps.renderer.MapRenderer;

public class GLSurfaceViewMapRenderer extends SurfaceViewMapRenderer {

  public GLSurfaceViewMapRenderer(Context context,
                                @NonNull MapVinaGLSurfaceView surfaceView,
                                String localIdeographFontFamily) {
    super(context, surfaceView, localIdeographFontFamily);

    surfaceView.setEGLContextFactory(new EGLContextFactory());
    surfaceView.setEGLWindowSurfaceFactory(new EGLWindowSurfaceFactory());
    surfaceView.setEGLConfigChooser(new EGLConfigChooser());
    surfaceView.setRenderer(this);
    surfaceView.setRenderingRefreshMode(MapRenderer.RenderingRefreshMode.WHEN_DIRTY);
    surfaceView.setPreserveEGLContextOnPause(true);
  }
}
