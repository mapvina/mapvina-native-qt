package com.mapvina.android.module.loader;

import com.mapvina.android.LibraryLoader;
import com.mapvina.android.LibraryLoaderProvider;

/**
 * Concrete implementation of a native library loader.
 * <p>
 * </p>
 */
public class LibraryLoaderProviderImpl implements LibraryLoaderProvider {

  /**
   * Creates and returns a the default Library Loader.
   *
   * @return the default library loader
   */
  @Override
  public LibraryLoader getDefaultLibraryLoader() {
    return new SystemLibraryLoader();
  }

  /**
   * Concrete implementation of a LibraryLoader using System.loadLibrary.
   */
  private static class SystemLibraryLoader extends LibraryLoader {
    @Override
    public void load(String name) {
      System.loadLibrary(name);
    }
  }
}
