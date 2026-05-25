package com.mapvina.android.testapp.style;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.mapvina.android.testapp.activity.BaseTest;
import com.mapvina.android.testapp.activity.style.RuntimeStyleTimingTestActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Basic smoke tests for adding Layer and Source as early as possible (in onCreate)
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class RuntimeStyleTimingTests extends BaseTest {

  @Override
  protected Class getActivityClass() {
    return RuntimeStyleTimingTestActivity.class;
  }

  @Test
  public void testGetAddRemoveLayer() {
    validateTestSetup();
    // We're good if it didn't crash
  }
}
