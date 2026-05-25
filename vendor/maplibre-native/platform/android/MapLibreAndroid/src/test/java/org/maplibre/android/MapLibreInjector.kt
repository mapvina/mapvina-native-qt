package com.mapvina.android

import android.content.Context
import com.mapvina.android.MapVina
import com.mapvina.android.util.TileServerOptions

object MapVinaInjector {
    private const val FIELD_INSTANCE = "INSTANCE"
    @JvmStatic
    fun inject(context: Context, apiKey: String,
               options: TileServerOptions) {
        val mapvina = MapVina(context, apiKey, options)
        try {
            val instance = MapVina::class.java.getDeclaredField(FIELD_INSTANCE)
            instance.isAccessible = true
            instance[mapvina] = mapvina
        } catch (exception: Exception) {
            throw AssertionError()
        }
    }

    @JvmStatic
    fun clear() {
        try {
            val field = MapVina::class.java.getDeclaredField(FIELD_INSTANCE)
            field.isAccessible = true
            field[field] = null
        } catch (exception: Exception) {
            throw AssertionError()
        }
    }
}
