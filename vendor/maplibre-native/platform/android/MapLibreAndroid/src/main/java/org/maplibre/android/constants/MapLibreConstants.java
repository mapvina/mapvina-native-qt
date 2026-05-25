package com.mapvina.android.constants;

import java.util.Locale;

/**
 * MapVinaConstants exposes MapVina related constants
 */
public class MapVinaConstants {

  /**
   * Default Locale for data processing (ex: String.toLowerCase(MAPBOX_LOCALE, "foo"))
   */
  public static final Locale MAPVINA_LOCALE = Locale.US;

  /**
   * The name of the desired preferences file for Android's SharedPreferences.
   */
  public static final String MAPVINA_SHARED_PREFERENCES = "MapboxSharedPreferences";

  /**
   * Key used to switch storage to external in AndroidManifest.xml
   */
  public static final String KEY_META_DATA_SET_STORAGE_EXTERNAL = "com.mapbox.SetStorageExternal";

  /**
   * Default value for KEY_META_DATA_SET_STORAGE_EXTERNAL (default is internal storage)
   */
  public static final boolean DEFAULT_SET_STORAGE_EXTERNAL = false;

  /**
   * Key used to switch Tile Download Measuring on/off in AndroidManifest.xml
   */
  public static final String KEY_META_DATA_MEASURE_TILE_DOWNLOAD_ON = "com.mapbox.MeasureTileDownloadOn";

  /**
   * Default value for KEY_META_DATA_MEASURE_TILE_DOWNLOAD_ON (default is off)
   */
  public static final boolean DEFAULT_MEASURE_TILE_DOWNLOAD_ON = false;

  /**
   * Default value for font fallback for local ideograph fonts
   */
  public static final String DEFAULT_FONT = "sans-serif";

  /**
   * Unmeasured state
   */
  public static final float UNMEASURED = -1f;

  /**
   * Default animation time
   */
  public static final int ANIMATION_DURATION = 300;

  /**
   * Default short animation time
   */
  public static final int ANIMATION_DURATION_SHORT = 150;

  /**
   * Animation time of a fling gesture
   */
  public static final long ANIMATION_DURATION_FLING_BASE = ANIMATION_DURATION_SHORT;

  /**
   * Velocity threshold for a fling gesture
   */
  public static final long VELOCITY_THRESHOLD_IGNORE_FLING = 1000;

  /**
   * Vertical angle threshold for a horizontal disabled fling gesture
   */
  public static final long ANGLE_THRESHOLD_IGNORE_VERTICAL_FLING = 75;

  /**
   * Value by which the default rotation threshold will be increased when scaling
   *
   * @deprecated unused, see {@link com.mapvina.android.maps.UiSettings#setDisableRotateWhenScaling(boolean)}
   */
  @Deprecated
  public static final float ROTATION_THRESHOLD_INCREASE_WHEN_SCALING = 25f;

  /**
   * Maximum absolute zoom change for multi-pointer scale velocity animation
   */
  public static final double MAX_ABSOLUTE_SCALE_VELOCITY_CHANGE = 2.5;

  /**
   * Maximum possible zoom change during the quick zoom gesture executed across the whole screen
   */
  public static final double QUICK_ZOOM_MAX_ZOOM_CHANGE = 4.0;

  /**
   * Scale velocity animation duration multiplier.
   */
  public static final double SCALE_VELOCITY_ANIMATION_DURATION_MULTIPLIER = 150;

  /**
   * Minimum angular velocity for rotation animation
   *
   * @deprecated unused, see {@link #ROTATE_VELOCITY_RATIO_THRESHOLD}
   */
  @Deprecated
  public static final float MINIMUM_ANGULAR_VELOCITY = 1.5f;

  /**
   * Last scale span delta to XY velocity ratio required to execute scale velocity animation.
   */
  public static final double SCALE_VELOCITY_RATIO_THRESHOLD = 4 * 1e-3;

  /**
   * Last rotation delta to XY velocity ratio required to execute rotation velocity animation.
   */
  public static final double ROTATE_VELOCITY_RATIO_THRESHOLD = 2.2 * 1e-4;

  /**
   * Time within which user needs to lift fingers for velocity animation to start.
   */
  public static final long SCHEDULED_ANIMATION_TIMEOUT = 150L;

  /**
   * Maximum angular velocity for rotation animation
   */
  public static final float MAXIMUM_ANGULAR_VELOCITY = 30f;

  /**
   * Factor to calculate tilt change based on pixel change during shove gesture.
   */
  public static final float SHOVE_PIXEL_CHANGE_FACTOR = 0.1f;

  /**
   * The currently supported minimum zoom level.
   */
  public static final float MINIMUM_ZOOM = 0.0f;

  /**
   * The currently supported maximum zoom level.
   */
  public static final float MAXIMUM_ZOOM = 25.5f;

  /**
   * The currently supported minimum pitch level.
   */
  public static final float MINIMUM_PITCH = 0.0f;

  /**
   * The currently supported maximum pitch level.
   */
  public static final float MAXIMUM_PITCH = 60.0f;

  /**
   * The currently supported maximum tilt value.
   */
  public static final double MAXIMUM_TILT = 60;

  /**
   * The currently supported minimum tilt value.
   */
  public static final double MINIMUM_TILT = 0;

  /**
   * The currently supported maximum direction
   */
  public static final double MAXIMUM_DIRECTION = 360;

  /**
   * The currently supported minimum direction
   */
  public static final double MINIMUM_DIRECTION = 0;

  /**
   * The currently used minimum scale factor to clamp to when a quick zoom gesture occurs
   *
   * @deprecated unused
   */
  @Deprecated
  public static final float MINIMUM_SCALE_FACTOR_CLAMP = 0.00f;

  /**
   * The currently used maximum scale factor to clamp to when a quick zoom gesture occurs
   *
   * @deprecated unused
   */
  @Deprecated
  public static final float MAXIMUM_SCALE_FACTOR_CLAMP = 0.15f;

  /**
   * Zoom value multiplier for scale gestures.
   */
  public static final float ZOOM_RATE = 0.65f;

  /**
   * Fragment Argument Key for MapVinaMapOptions
   */
  public static final String FRAG_ARG_MAPVINAMAPOPTIONS = "MapVinaMapOptions";

  /**
   * Layer Id of annotations layer
   */
  public static final String LAYER_ID_ANNOTATIONS = "com.mapvina.annotations.points";

  // Save instance state keys
  public static final String STATE_HAS_SAVED_STATE = "mapvina_savedState";
  public static final String STATE_CAMERA_POSITION = "mapvina_cameraPosition";
  public static final String STATE_ZOOM_ENABLED = "mapvina_zoomEnabled";
  public static final String STATE_SCROLL_ENABLED = "mapvina_scrollEnabled";
  public static final String STATE_HORIZONAL_SCROLL_ENABLED = "mapvina_horizontalScrollEnabled";
  public static final String STATE_ROTATE_ENABLED = "mapvina_rotateEnabled";
  public static final String STATE_TILT_ENABLED = "mapvina_tiltEnabled";
  public static final String STATE_DOUBLE_TAP_ENABLED = "mapvina_doubleTapEnabled";
  public static final String STATE_QUICK_ZOOM_ENABLED = "mapvina_quickZoom";
  public static final String STATE_ZOOM_RATE = "mapvina_zoomRate";
  public static final String STATE_DEBUG_ACTIVE = "mapvina_debugActive";
  public static final String STATE_COMPASS_ENABLED = "mapvina_compassEnabled";
  public static final String STATE_COMPASS_GRAVITY = "mapvina_compassGravity";
  public static final String STATE_COMPASS_MARGIN_LEFT = "mapvina_compassMarginLeft";
  public static final String STATE_COMPASS_MARGIN_TOP = "mapvina_compassMarginTop";
  public static final String STATE_COMPASS_MARGIN_RIGHT = "mapvina_compassMarginRight";
  public static final String STATE_COMPASS_MARGIN_BOTTOM = "mapvina_compassMarginBottom";
  public static final String STATE_COMPASS_FADE_WHEN_FACING_NORTH = "mapvina_compassFade";
  public static final String STATE_COMPASS_IMAGE_BITMAP = "mapvina_compassImage";
  public static final String STATE_LOGO_GRAVITY = "mapvina_logoGravity";
  public static final String STATE_LOGO_MARGIN_LEFT = "mapvina_logoMarginLeft";
  public static final String STATE_LOGO_MARGIN_TOP = "mapvina_logoMarginTop";
  public static final String STATE_LOGO_MARGIN_RIGHT = "mapvina_logoMarginRight";
  public static final String STATE_LOGO_MARGIN_BOTTOM = "mapvina_logoMarginBottom";
  public static final String STATE_LOGO_ENABLED = "mapvina_logoEnabled";
  public static final String STATE_ATTRIBUTION_GRAVITY = "mapvina_attrGravity";
  public static final String STATE_ATTRIBUTION_MARGIN_LEFT = "mapvina_attrMarginLeft";
  public static final String STATE_ATTRIBUTION_MARGIN_TOP = "mapvina_attrMarginTop";
  public static final String STATE_ATTRIBUTION_MARGIN_RIGHT = "mapvina_attrMarginRight";
  public static final String STATE_ATTRIBUTION_MARGIN_BOTTOM = "mapvina_atrrMarginBottom";
  public static final String STATE_ATTRIBUTION_ENABLED = "mapvina_atrrEnabled";
  public static final String STATE_DESELECT_MARKER_ON_TAP = "mapvina_deselectMarkerOnTap";
  public static final String STATE_USER_FOCAL_POINT = "mapvina_userFocalPoint";
  public static final String STATE_SCALE_ANIMATION_ENABLED = "mapvina_scaleAnimationEnabled";
  public static final String STATE_ROTATE_ANIMATION_ENABLED = "mapvina_rotateAnimationEnabled";
  public static final String STATE_FLING_ANIMATION_ENABLED = "mapvina_flingAnimationEnabled";
  public static final String STATE_INCREASE_ROTATE_THRESHOLD = "mapvina_increaseRotateThreshold";
  public static final String STATE_DISABLE_ROTATE_WHEN_SCALING = "mapvina_disableRotateWhenScaling";
  public static final String STATE_INCREASE_SCALE_THRESHOLD = "mapvina_increaseScaleThreshold";
}
