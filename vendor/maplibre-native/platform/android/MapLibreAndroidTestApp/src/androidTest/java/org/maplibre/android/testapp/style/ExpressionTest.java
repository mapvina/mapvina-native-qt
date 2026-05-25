package com.mapvina.android.testapp.style;

import org.junit.Ignore;
import com.mapvina.geojson.Feature;
import com.mapvina.geojson.Point;
import com.mapvina.android.geometry.LatLng;
import com.mapvina.android.style.expressions.Expression;
import com.mapvina.android.style.layers.CircleLayer;
import com.mapvina.android.style.layers.FillLayer;
import com.mapvina.android.style.layers.Layer;
import com.mapvina.android.style.layers.SymbolLayer;
import com.mapvina.android.style.sources.GeoJsonSource;
import com.mapvina.android.style.sources.Source;
import com.mapvina.android.style.types.Formatted;
import com.mapvina.android.style.types.FormattedSection;
import com.mapvina.android.testapp.R;
import com.mapvina.android.testapp.activity.EspressoTest;
import com.mapvina.android.testapp.utils.ResourceUtils;
import com.mapvina.android.testapp.utils.TestingAsyncUtils;
import com.mapvina.android.utils.ColorUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.graphics.Color;

import java.util.HashMap;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import timber.log.Timber;

import static com.mapvina.android.style.expressions.Expression.FormatOption.formatFontScale;
import static com.mapvina.android.style.expressions.Expression.FormatOption.formatTextColor;
import static com.mapvina.android.style.expressions.Expression.FormatOption.formatTextFont;
import static com.mapvina.android.style.expressions.Expression.NumberFormatOption.currency;
import static com.mapvina.android.style.expressions.Expression.NumberFormatOption.locale;
import static com.mapvina.android.style.expressions.Expression.NumberFormatOption.maxFractionDigits;
import static com.mapvina.android.style.expressions.Expression.NumberFormatOption.minFractionDigits;
import static com.mapvina.android.style.expressions.Expression.collator;
import static com.mapvina.android.style.expressions.Expression.color;
import static com.mapvina.android.style.expressions.Expression.eq;
import static com.mapvina.android.style.expressions.Expression.exponential;
import static com.mapvina.android.style.expressions.Expression.format;
import static com.mapvina.android.style.expressions.Expression.formatEntry;
import static com.mapvina.android.style.expressions.Expression.get;
import static com.mapvina.android.style.expressions.Expression.interpolate;
import static com.mapvina.android.style.expressions.Expression.literal;
import static com.mapvina.android.style.expressions.Expression.match;
import static com.mapvina.android.style.expressions.Expression.number;
import static com.mapvina.android.style.expressions.Expression.numberFormat;
import static com.mapvina.android.style.expressions.Expression.rgb;
import static com.mapvina.android.style.expressions.Expression.rgba;
import static com.mapvina.android.style.expressions.Expression.step;
import static com.mapvina.android.style.expressions.Expression.stop;
import static com.mapvina.android.style.expressions.Expression.string;
import static com.mapvina.android.style.expressions.Expression.switchCase;
import static com.mapvina.android.style.expressions.Expression.toColor;
import static com.mapvina.android.style.expressions.Expression.toPadding;
import static com.mapvina.android.style.expressions.Expression.zoom;
import static com.mapvina.android.style.layers.PropertyFactory.circleColor;
import static com.mapvina.android.style.layers.PropertyFactory.fillAntialias;
import static com.mapvina.android.style.layers.PropertyFactory.fillColor;
import static com.mapvina.android.style.layers.PropertyFactory.fillOutlineColor;
import static com.mapvina.android.style.layers.PropertyFactory.iconPadding;
import static com.mapvina.android.style.layers.PropertyFactory.textColor;
import static com.mapvina.android.style.layers.PropertyFactory.textField;
import static com.mapvina.android.testapp.action.MapVinaMapAction.invoke;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ExpressionTest extends EspressoTest {
  private FillLayer layer;

  @Test
  public void testConstantExpressionConversion() {
    validateTestSetup();
    setupStyle();
    Timber.i("camera function");

    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      // create color expression
      Expression inputExpression = rgba(255.0f, 0.0f, 0.0f, 1.0f);

      // set color expression
      layer.setProperties(
              fillColor(inputExpression)
      );

      // get color value
      int color = layer.getFillColor().getColorInt();

      // compare
      assertEquals("input expression should match", Color.RED, color);
    });
  }

  @Test
  public void testGetExpressionWrapping() {
    validateTestSetup();
    setupStyle();
    Timber.i("camera function");

    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      // create get expression
      Expression inputExpression = get("fill");

      // set get expression
      layer.setProperties(
              fillColor(inputExpression)
      );

      // get actual expression
      Expression actualExpression = layer.getFillColor().getExpression();

      // create wrapped expected expression
      Expression expectedExpression = toColor(get("fill"));

      // compare
      assertEquals("input expression should match", expectedExpression, actualExpression);
    });
  }

  @Test
  public void testCameraFunction() {
    validateTestSetup();
    setupStyle();
    Timber.i("camera function");

    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      // create camera function expression
      Expression inputExpression = interpolate(
              exponential(0.5f), zoom(),
              stop(1.0f, rgba(255.0f, 0.0f, 0.0f, 1.0f)),
              stop(5.0f, rgba(0.0f, 0.0f, 255.0f, 1.0f)),
              stop(10.0f, rgba(0.0f, 255.0f, 0.0f, 1.0f))
      );

      // set camera function expression
      layer.setProperties(
              fillColor(inputExpression)
      );

      // get camera function expression
      Expression outputExpression = layer.getFillColor().getExpression();

      // compare
      assertEquals("input expression should match", inputExpression, outputExpression);
    });
  }

  @Test
  public void testSourceFunction() {
    validateTestSetup();
    setupStyle();
    Timber.i("camera function");

    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      // create camera function expression
      Expression inputExpression = toColor(get("fill"));

      // set camera function expression
      layer.setProperties(
              fillColor(inputExpression)
      );

      // get camera function expression
      Expression outputExpression = layer.getFillColor().getExpression();

      // compare
      assertEquals("input expression should match", inputExpression, outputExpression);
    });
  }

  @Test
  public void testCompositeFunction() {
    validateTestSetup();
    setupStyle();
    Timber.i("camera function");

    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      // create camera function expression
      Expression inputExpression = step(zoom(),
              rgba(255.0f, 255.0f, 255.0f, 1.0f),
              stop(7.0f, match(
                      string(get("name")),
                      literal("Westerpark"), rgba(255.0f, 0.0f, 0.0f, 1.0f),
                      rgba(255.0f, 255.0f, 255.0f, 1.0f)
              )),
              stop(8.0f, match(
                      string(get("name")),
                      literal("Westerpark"), rgba(0.0f, 0.0f, 255.0f, 1.0f),
                      rgba(255.0f, 255.0f, 255.0f, 1.0f)
              ))
      );

      // set camera function expression
      layer.setProperties(
              fillColor(inputExpression)
      );

      // get camera function expression
      Expression outputExpression = layer.getFillColor().getExpression();

      // compare
      assertEquals("input expression should match", inputExpression, outputExpression);
    });
  }

  @Test
  public void testLiteralProperty() {
    validateTestSetup();
    setupStyle();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      layer.setProperties(
              fillColor(literal("#4286f4"))
      );
    });
  }

  @Test
  public void testLiteralMatchExpression() {
    validateTestSetup();
    setupStyle();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      Expression expression = match(literal("something"), literal(0f),
              stop("1", get("1")),
              stop("2", get("2")),
              stop("3", get("3")),
              stop("4", get("4"))
      );

      layer.setProperties(
              fillColor(expression)
      );
      expression.toArray();
    });
  }

  @Test
  public void testCollatorExpression() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);

      Expression expression1 = eq(literal("Łukasz"), literal("lukasz"), collator(true, true));
      Expression expression2 = eq(literal("Łukasz"), literal("lukasz"), collator(literal(false), eq(literal(1),
              literal(1)), literal("en")));
      Expression expression3 = eq(literal("Łukasz"), literal("lukasz"), collator(literal(false), eq(literal(2),
              literal(1))));

      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      Layer layer = new CircleLayer("layer", "source")
              .withProperties(circleColor(
                      switchCase(
                              expression1, literal(ColorUtils.colorToRgbaString(Color.GREEN)),
                              literal(ColorUtils.colorToRgbaString(Color.RED))
                      )
              ));
      mapvinaMap.getStyle().addLayer(layer);
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);
      assertFalse(mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer")
              .isEmpty());

      layer.setProperties(circleColor(
              switchCase(
                      expression2, literal(ColorUtils.colorToRgbaString(Color.GREEN)),
                      literal(ColorUtils.colorToRgbaString(Color.RED))
              )
      ));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);
      assertFalse(mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer")
              .isEmpty());

      layer.setProperties(circleColor(
              switchCase(
                      expression3, literal(ColorUtils.colorToRgbaString(Color.GREEN)),
                      literal(ColorUtils.colorToRgbaString(Color.RED))
              )
      ));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);
      assertFalse(mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer")
              .isEmpty());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testConstFormatExpression() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression expression = format(
              formatEntry("test")
      );
      layer.setProperties(textField(expression));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer")
              .isEmpty());
      assertNull(layer.getTextField().getExpression());
      assertEquals(new Formatted(new FormattedSection("test")), layer.getTextField().getValue());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testConstFormatExpressionFontScaleParam() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression expression = format(
              formatEntry("test", formatFontScale(1.75))
      );
      layer.setProperties(textField(expression));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer")
              .isEmpty());
      assertNull(layer.getTextField().getExpression());
      assertEquals(new Formatted(new FormattedSection("test", 1.75)), layer.getTextField().getValue());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testConstFormatExpressionTextFontParam() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression expression = format(
              formatEntry(
                      literal("test"),
                      formatTextFont(new String[]{"DIN Offc Pro Regular", "Arial Unicode MS Regular"})
              )
      );
      layer.setProperties(textField(expression));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(
              mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer").isEmpty()
      );
      assertNull(layer.getTextField().getExpression());
      assertEquals(new Formatted(
              new FormattedSection("test",
                      new String[]{"DIN Offc Pro Regular", "Arial Unicode MS Regular"})
      ), layer.getTextField().getValue());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testConstFormatExpressionTextColorParam() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression expression = format(
              formatEntry(
                      literal("test"),
                      formatTextColor(literal("yellow"))
              )
      );
      layer.setProperties(textField(expression));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(
              mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer").isEmpty()
      );
      assertNull(layer.getTextField().getExpression());
      assertEquals(new Formatted(
              new FormattedSection("test", null, null, "rgba(255,255,0,1)")
      ), layer.getTextField().getValue());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testConstFormatExpressionAllParams() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression expression = format(
              formatEntry(
                      "test",
                      formatFontScale(0.5),
                      formatTextFont(new String[]{"DIN Offc Pro Regular", "Arial Unicode MS Regular"}),
                      formatTextColor(rgb(126, 0, 0))
              )
      );
      layer.setProperties(textField(expression));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(
              mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer").isEmpty()
      );
      assertNull(layer.getTextField().getExpression());
      assertEquals(new Formatted(
              new FormattedSection("test",
                      0.5,
                      new String[]{"DIN Offc Pro Regular", "Arial Unicode MS Regular"},
                      "rgba(126,0,0,1)")
      ), layer.getTextField().getValue());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testConstFormatExpressionMultipleInputs() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression expression = format(
              formatEntry(
                      "test",
                      formatFontScale(1.5),
                      formatTextFont(new String[]{"DIN Offc Pro Regular", "Arial Unicode MS Regular"})
              ),
              formatEntry("\ntest2", formatFontScale(2), formatTextColor(Color.BLUE)),
              formatEntry("\ntest3", formatFontScale(2.5),
                      formatTextColor(toColor(literal("rgba(0, 128, 255, 0.5)"))))
      );
      layer.setProperties(textField(expression));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(
              mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer")
                      .isEmpty()
      );
      assertNull(layer.getTextField().getExpression());
      assertEquals(new Formatted(
              new FormattedSection("test", 1.5,
                      new String[]{"DIN Offc Pro Regular", "Arial Unicode MS Regular"}),
              new FormattedSection("\ntest2", 2.0, null, "rgba(0,0,255,1)"),
              new FormattedSection("\ntest3", 2.5, null, "rgba(0,128,255,0.5)")
      ), layer.getTextField().getValue());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testVariableFormatExpression() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      Feature feature = Feature.fromGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
      feature.addStringProperty("test_property", "test");
      feature.addNumberProperty("test_property_number", 1.5);
      feature.addStringProperty("test_property_color", "green");
      mapvinaMap.getStyle().addSource(new GeoJsonSource("source", feature));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression expression = format(
              formatEntry(
                      get("test_property"),
                      Expression.FormatOption.formatFontScale(number(get("test_property_number"))),
                      formatTextFont(new String[]{"Arial Unicode MS Regular", "DIN Offc Pro Regular"}),
                      formatTextColor(toColor(get("test_property_color")))
              )
      );
      layer.setProperties(textField(expression));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer")
              .isEmpty());
      assertEquals(expression, layer.getTextField().getExpression());
      assertNull(layer.getTextField().getValue());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testVariableFormatExpressionMultipleInputs() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      Feature feature = Feature.fromGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
      feature.addStringProperty("test_property", "test");
      feature.addNumberProperty("test_property_number", 1.5);
      feature.addStringProperty("test_property_color", "rgba(0, 255, 0, 1)");
      mapvinaMap.getStyle().addSource(new GeoJsonSource("source", feature));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression expression = format(
              formatEntry(
                      get("test_property"),
                      formatFontScale(1.25),
                      formatTextFont(new String[]{"Arial Unicode MS Regular", "DIN Offc Pro Regular"}),
                      formatTextColor(toColor(get("test_property_color")))
              ),
              formatEntry("\ntest2", formatFontScale(2))
      );
      layer.setProperties(textField(expression), textColor("rgba(128, 0, 0, 1)"));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer")
              .isEmpty());
      assertEquals(expression, layer.getTextField().getExpression());
      assertNull(layer.getTextField().getValue());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testFormatExpressionPlainTextCoercion() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      layer.setProperties(textField("test"));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer")
              .isEmpty());
      assertNull(layer.getTextField().getExpression());
      assertEquals(new Formatted(
              new FormattedSection("test")), layer.getTextField().getValue());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testTextFieldFormattedArgument() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Formatted formatted = new Formatted(
              new FormattedSection("test", 1.5),
              new FormattedSection("\ntest", 0.5, new String[]{"Arial Unicode MS Regular",
                "DIN Offc Pro Regular"}),
              new FormattedSection("test", null, null, "rgba(0,255,0,1)")
      );
      layer.setProperties(textField(formatted), textColor("rgba(128,0,0,1)"));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(mapvinaMap.getProjection().toScreenLocation(latLng), "layer")
              .isEmpty());
      assertNull(layer.getTextField().getExpression());
      assertEquals(formatted, layer.getTextField().getValue());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testNumberFormatCurrencyExpression() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      layer.setProperties(
              textField(
                      numberFormat(12.345, locale("en-US"), currency("USD"))
              )
      );
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(
              mapvinaMap.getProjection().toScreenLocation(latLng), "layer").isEmpty()
      );
      assertNull(layer.getTextField().getExpression());
      assertEquals("$12.35", layer.getTextField().getValue().getFormattedSections()[0].getText());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testNumberFormatMaxExpression() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      layer.setProperties(
              textField(
                      numberFormat(12.34567890, maxFractionDigits(5), minFractionDigits(0))
              )
      );
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(
              mapvinaMap.getProjection().toScreenLocation(latLng), "layer").isEmpty()
      );
      assertNull(layer.getTextField().getExpression());
      assertEquals("12.34568", layer.getTextField().getValue().getFormattedSections()[0].getText());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testNumberFormatMinExpression() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      layer.setProperties(
              textField(
                      numberFormat(12.0000001, maxFractionDigits(5), minFractionDigits(0))
              )
      );
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(
              mapvinaMap.getProjection().toScreenLocation(latLng), "layer").isEmpty()
      );
      assertNull(layer.getTextField().getExpression());
      assertEquals("12", layer.getTextField().getValue().getFormattedSections()[0].getText());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testNumberFormatLocaleExpression() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle()
              .addSource(new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      layer.setProperties(
              textField(
                      numberFormat(12.0000001, locale("nl-BE"), maxFractionDigits(5),
                              minFractionDigits(1))
              )
      );
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(
              mapvinaMap.getProjection().toScreenLocation(latLng), "layer").isEmpty()
      );
      assertNull(layer.getTextField().getExpression());
      assertEquals("12,0", layer.getTextField().getValue().getFormattedSections()[0].getText());
    });
  }

  @Test
  @Ignore("https://github.com/mapvina/mapvina-native/issues/2437")
  public void testNumberFormatNonConstantExpression() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      Feature feature = Feature.fromGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
      feature.addNumberProperty("number_value", 12.345678);
      feature.addStringProperty("locale_value", "nl-BE");
      feature.addNumberProperty("max_value", 5);
      feature.addNumberProperty("min_value", 1);


      mapvinaMap.getStyle().addSource(new GeoJsonSource("source", feature));
      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression numberFormatExpression = numberFormat(
              number(number(get("number_value"))),
              locale(string(get("locale_value"))),
              maxFractionDigits(number(get("max_value"))),
              minFractionDigits(number(get("min_value")))
      );

      layer.setProperties(textField(numberFormatExpression));
      TestingAsyncUtils.INSTANCE.waitForLayer(uiController, mapView);

      assertFalse(mapvinaMap.queryRenderedFeatures(
              mapvinaMap.getProjection().toScreenLocation(latLng), "layer").isEmpty()
      );

      assertNotNull(layer.getTextField().getExpression());

      // Expressions evaluated to string are wrapped by a format expression, take array index 1 to get original
      Object[] returnExpression = (Object[]) layer.getTextField().getExpression().toArray()[1];
      Object[] setExpression = numberFormatExpression.toArray();
      assertEquals("Number format should match", returnExpression[0], setExpression[0]);
      assertArrayEquals("Get value expression should match",
              (Object[]) returnExpression[1],
              (Object[]) setExpression[1]
      );

      // number format objects
      HashMap<String, Object> returnMap = (HashMap<String, Object>) returnExpression[2];
      HashMap<String, Object> setMap = (HashMap<String, Object>) returnExpression[2];

      assertArrayEquals("Number format min fraction digits should match ",
              (Object[]) returnMap.get("min-fraction-digits"),
              (Object[]) setMap.get("min-fraction-digits")
      );

      assertArrayEquals("Number format max fraction digits should match ",
              (Object[]) returnMap.get("max-fraction-digits"),
              (Object[]) setMap.get("max-fraction-digits")
      );

      assertArrayEquals("Number format min fraction digits should match ",
              (Object[]) returnMap.get("locale"),
              (Object[]) setMap.get("locale")
      );
    });

  }

  /**
   * Regression test for #15532
   */
  @Test
  public void testDoubleConversion() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle().addSource(
              new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
      );

      CircleLayer layer = new CircleLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression input = interpolate(
              exponential(0.5f), zoom(),
              stop(-0.1, color(Color.RED)),
              stop(0, color(Color.BLUE))
      );

      layer.setProperties(circleColor(input));

      Expression output = layer.getCircleColor().getExpression();
      assertArrayEquals("Expression should match", input.toArray(), output.toArray());
    });
  }

  @Test
  public void testToPaddingExpression() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle().addSource(
              new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
      );

      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      // Automatic usage with iconPadding property, analogous to testGetExpressionWrapping()
      {
        Expression input = get("value");
        layer.setProperties(iconPadding(input));

        Expression expectedOuput = toPadding(input);
        Expression output = layer.getIconPadding().getExpression();
        assertNotNull(output);
        assertArrayEquals("Expression should match", expectedOuput.toArray(), output.toArray());
      }

      // Same within interpolate expression
      {
        Expression input = interpolate(
                exponential(0.5f), zoom(),
                stop(-0.1, get("value")),
                stop(0, get("value"))
        );
        layer.setProperties(iconPadding(input));

        Expression expectedOutput = interpolate(
                exponential(0.5f), zoom(),
                stop(-0.1, toPadding(get("value"))),
                stop(0, toPadding(get("value")))
        );
        Expression output = layer.getIconPadding().getExpression();
        assertNotNull(output);
        assertArrayEquals("Expression should match", expectedOutput.toArray(), output.toArray());
      }
    });
  }

  @Test
  public void testToPaddingResult() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle().addSource(
              new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
      );

      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression input = Expression.toPadding(Expression.literal(new Float[] { 7.5f, 10.0f, 1.0f}));
      layer.setProperties(iconPadding(input));

      assertNull("Expression should be null", layer.getIconPadding().getExpression());
      assertArrayEquals(
              "Padding value should match",
              new Float[] { 7.5f, 10.0f, 1.0f, 10.0f },
              layer.getIconPadding().getValue());
    });
  }

  @Test
  public void testToPaddingError() {
    validateTestSetup();
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      LatLng latLng = new LatLng(51, 17);
      mapvinaMap.getStyle().addSource(
              new GeoJsonSource("source", Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
      );

      SymbolLayer layer = new SymbolLayer("layer", "source");
      mapvinaMap.getStyle().addLayer(layer);

      Expression input = toPadding(literal("invalid"));
      layer.setProperties(iconPadding(input));

      assertNull("Expression should be null", layer.getIconPadding().getExpression());
      assertNull("Padding value should be null", layer.getIconPadding().getValue());
    });
  }

  private void setupStyle() {
    invoke(mapvinaMap, (uiController, mapvinaMap) -> {
      // Add a source
      Source source;
      source = new GeoJsonSource("amsterdam-parks-source",
              ResourceUtils.readRawResource(rule.getActivity(), R.raw.amsterdam));
      mapvinaMap.getStyle().addSource(source);

      // Add a fill layer
      mapvinaMap.getStyle().addLayer(layer = new FillLayer("amsterdam-parks-layer", source.getId())
              .withProperties(
                      fillColor(rgba(0.0f, 0.0f, 0.0f, 0.5f)),
                      fillOutlineColor(rgb(0, 0, 255)),
                      fillAntialias(true)
              )
      );
    });
  }
}
