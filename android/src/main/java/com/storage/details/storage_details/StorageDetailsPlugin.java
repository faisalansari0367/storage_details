package com.storage.details.storage_details;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.BinaryMessenger;

//imports
import android.app.Activity;
import android.content.Context;
import java.util.HashMap;
import java.util.ArrayList;
import android.os.Environment;
import android.os.StatFs;
import java.io.File;
import android.net.Uri;
import android.content.Intent;

/** StorageDetailsPlugin */
public class StorageDetailsPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native
  /// Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine
  /// and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel methodChannel;
  private Context context;
  private Context applicationContext;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final StorageDetailsPlugin instance = new StorageDetailsPlugin();
    instance.onAttachedToEngine(registrar.context(), registrar.messenger());
  }

  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    onAttachedToEngine(binding.getApplicationContext(), binding.getBinaryMessenger());
  }

  private void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger) {
    this.applicationContext = applicationContext;
    methodChannel = new MethodChannel(messenger, "storage_details");
    methodChannel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("getStorageDetails")) {
      result.success(getSpace());
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(FlutterPluginBinding binding) {
    applicationContext = null;
    methodChannel.setMethodCallHandler(null);
    methodChannel = null;
  }

  ArrayList<HashMap> getSpace() {
    String path = Environment.getExternalStorageDirectory().getPath();
    final ArrayList<HashMap> extRootPaths = new ArrayList<>();
    final ArrayList<HashMap> removable = new ArrayList<>();
    final File[] appsDir = applicationContext.getExternalFilesDirs(null);
    
    for (final File file : appsDir) {
      String a = file.getAbsolutePath();
      if (Environment.isExternalStorageRemovable(file)) {
        String value = Environment.getExternalStorageState(file);
        if (!value.equals(Environment.MEDIA_UNMOUNTED)) {
          removable.add(getSpaceDetails(a));
        }
      } else {
        extRootPaths.add(getSpaceDetails(a));
      }
    }
    if (removable.isEmpty()) {
      return extRootPaths;
    } else {
      extRootPaths.addAll(removable);
      return extRootPaths;
    }
  }

  public static HashMap<String, String> getSpaceDetails(String path) {
    StatFs stat = new StatFs(path);
    long blockSize = stat.getBlockSizeLong();
    long totalBlocks = stat.getBlockCountLong();
    long availableBlocks = stat.getAvailableBlocksLong();
    long availableBytes = blockSize * availableBlocks;
    long totalBytes = totalBlocks * blockSize;

    HashMap<String, String> map = new HashMap<String, String>();
    map.put("total", totalBytes + "");
    map.put("free", availableBytes + "");
    map.put("path", path);
    return map;
  }
}
