package com.mapvina.android.maps;

import android.graphics.RectF;

import com.mapvina.android.annotations.Annotation;

import java.util.List;

interface ShapeAnnotations {

  List<Annotation> obtainAllIn(RectF rectF);

}
