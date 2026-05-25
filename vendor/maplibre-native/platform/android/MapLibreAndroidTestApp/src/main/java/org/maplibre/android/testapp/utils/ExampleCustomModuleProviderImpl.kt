package com.mapvina.android.testapp.utils

import com.mapvina.android.LibraryLoaderProvider
import com.mapvina.android.ModuleProvider
import com.mapvina.android.http.HttpRequest
import com.mapvina.android.module.loader.LibraryLoaderProviderImpl
import com.mapvina.android.testapp.utils.ExampleHttpRequestImpl

/*
 * An example implementation of the ModuleProvider. This is useful primarily for providing
 * a custom implementation of HttpRequest used by the core.
 */
class ExampleCustomModuleProviderImpl : ModuleProvider {
    override fun createHttpRequest(): HttpRequest {
        return ExampleHttpRequestImpl()
    }

    override fun createLibraryLoaderProvider(): LibraryLoaderProvider {
        return LibraryLoaderProviderImpl()
    }
}
