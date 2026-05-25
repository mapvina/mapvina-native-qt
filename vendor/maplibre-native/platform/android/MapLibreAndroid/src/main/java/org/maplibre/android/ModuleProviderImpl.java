package com.mapvina.android;

import androidx.annotation.NonNull;

import com.mapvina.android.http.HttpRequest;
import com.mapvina.android.module.http.HttpRequestImpl;
import com.mapvina.android.module.loader.LibraryLoaderProviderImpl;

public class ModuleProviderImpl implements ModuleProvider {

  @Override
  @NonNull
  public HttpRequest createHttpRequest() {
    return new HttpRequestImpl();
  }

  @NonNull
  @Override
  public LibraryLoaderProvider createLibraryLoaderProvider() {
    return new LibraryLoaderProviderImpl();
  }
}
