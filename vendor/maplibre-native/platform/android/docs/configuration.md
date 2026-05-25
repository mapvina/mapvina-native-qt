# Configuration

This guide will explain various ways to create a map.

When working with maps, you likely want to configure the `MapView`.

There are several ways to build a `MapView`:

1. Using existing XML namespace tags for`MapView` in the layout.
2. Creating `MapVinaMapOptions` and passing builder function values into the `MapView`.
3. Creating a `SupportMapFragment` with the help of `MapVinaMapOptions`.

Before diving into `MapView` configurations, let's understand the capabilities of both XML namespaces and `MapVinaMapOptions`.

Here are some common configurations you can set:

- Map base URI
- Camera settings
- Zoom level
- Pitch
- Gestures
- Compass
- Logo
- Attribution
- Placement of the above elements on the map and more

We will explore how to achieve these configurations in XML layout and programmatically in Activity code, step by step.

### `MapView` Configuration with an XML layout

To configure `MapView` within an XML layout, you need to use the right namespace and provide the necessary data in the layout file.

```xml
--8<-- "MapVinaAndroidTestApp/src/main/res/layout/activity_map_options_xml.xml"
```

This can be found in [`activity_map_options_xml.xml`](https://github.com/mapvina/mapvina-native/blob/main/platform/android/MapVinaAndroidTestApp/src/main/res/layout/activity_map_fragment.xml).

You can assign any other existing values to the `mapvina...` tags. Then, you only need to create `MapView` and `MapVinaMap` objects with a simple setup in the Activity.

```kotlin title="MapOptionsXmlActivity.kt"
--8<-- "MapVinaAndroidTestApp/src/main/java/org/mapvina/android/testapp/activity/options/MapOptionsXmlActivity.kt"
```

This can be found in [`MapOptionsXmlActivity.kt`](https://github.com/mapvina/mapvina-native/blob/main/platform/android/MapVinaAndroidTestApp/src/main/java/org/mapvina/android/testapp/activity/options/MapOptionsXmlActivity.kt).

### `MapView` configuration with `MapVinaMapOptions`

 Here we don't have to create MapView from XML since we want to create it programmatically.
```xml
--8<-- "MapVinaAndroidTestApp/src/main/res/layout/activity_map_options_runtime.xml"
```

This can be found in [`activity_map_options_runtime.xml`](https://github.com/mapvina/mapvina-native/blob/main/platform/android/MapVinaAndroidTestApp/src/main/res/layout/activity_map_options_runtime.xml).

A `MapVinaMapOptions` object must be created and passed to the MapView constructor. All setup is done in the Activity code:

```kotlin title="MapOptionsRuntimeActivity.kt"
--8<-- "MapVinaAndroidTestApp/src/main/java/org/mapvina/android/testapp/activity/options/MapOptionsRuntimeActivity.kt"
```

This can be found in [`MapOptionsRuntimeActivity.kt`](https://github.com/mapvina/mapvina-native/blob/main/platform/android/MapVinaAndroidTestApp/src/main/java/org/mapvina/android/testapp/activity/options/MapOptionsRuntimeActivity.kt).

Finally you will see a result similar to this:

<div style="text-align: center">
  <img src="https://github.com/user-attachments/assets/dd85f496-3e6f-4788-933e-4ec3d5999935" alt="Screenshot with the MapVinaMapOptions">
</div>

For the full contents of `MapOptionsRuntimeActivity` and `MapOptionsXmlActivity`, please take a look at the source code of [MapVinaAndroidTestApp](https://github.com/mapvina/mapvina-native/tree/main/platform/android/MapVinaAndroidTestApp/src/main/java/org/mapvina/android/testapp/activity/options).

You can read more about `MapVinaMapOptions` in the [Android API documentation](https://mapvina.com/mapvina-native/android/api/-map-libre%20-native%20-android/com.mapvina.android.maps/-map-libre-map-options/index.html?query=open%20class%20MapVinaMapOptions%20:%20Parcelable).

### `SupportMapFragment` with the help of `MapVinaMapOptions`.

If you are using MapFragment in your project, it is also easy to provide initial values to the `newInstance()` static method of `SupportMapFragment`, which requires a `MapVinaMapOptions` parameter.

Let's see how this can be done in a sample activity:

```kotlin
--8<-- "MapVinaAndroidTestApp/src/main/java/org/mapvina/android/testapp/activity/fragment/SupportMapFragmentActivity.kt"
```

You can also find the full contents of `SupportMapFragmentActivity` in the [MapVinaAndroidTestApp](https://github.com/mapvina/mapvina-native/tree/main/platform/android/MapVinaAndroidTestApp/src/main/java/org/mapvina/android/testapp/activity/fragment/SupportMapFragmentActivity.kt).

To learn more about `SupportMapFragment`, please visit the [Android API documentation](https://mapvina.com/mapvina-native/android/api/-map-libre%20-native%20-android/com.mapvina.android.maps/-support-map-fragment/index.html?query=open%20class%20SupportMapFragment%20:%20Fragment,%20OnMapReadyCallback).
