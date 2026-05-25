package com.mapvina.android.module.http

import com.mapvina.android.MapVinaInjector
import com.mapvina.android.utils.ConfigUtils
import io.mockk.mockk
import okhttp3.Call
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HttpRequestUtilTest {

    @Test
    fun replaceHttpClient() {
        MapVinaInjector.inject(mockk(relaxed = true), "", ConfigUtils.getMockedOptions())

        assertEquals(HttpRequestImpl.DEFAULT_CLIENT, HttpRequestImpl.client)

        val httpMock = mockk<Call.Factory>()
        HttpRequestUtil.setOkHttpClient(httpMock)
        assertEquals(
            "Http client should have set to the mocked client",
            httpMock,
            HttpRequestImpl.client
        )

        HttpRequestUtil.setOkHttpClient(null)
        assertEquals(
            "Http client should have been reset to the default client",
            HttpRequestImpl.DEFAULT_CLIENT,
            HttpRequestImpl.client
        )

        MapVinaInjector.clear()
    }
}
