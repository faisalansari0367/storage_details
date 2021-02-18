package com.storage.details.storage_details;

import io.flutter.plugin.common.EventChannel;
import android.content.Context;
import android.content.Intent;
import android.content.ActivityNotFoundException;

import android.os.Looper;
import android.os.Handler;

import android.net.Uri;
import java.io.File;

import android.os.Environment;
import android.os.FileObserver;
import android.os.StatFs;

// private EventChannel.EventSink events;

public class EventHandler implements EventChannel.StreamHandler {
  private Handler mainHandler = new Handler(Looper.getMainLooper());
  private Context applicationContext;
  private FileObserver observer;

  public EventHandler(Context context) {
    this.applicationContext = context;
  }

  @Override
  public void onListen(Object args, final EventChannel.EventSink events) {
    File dir = Environment.getExternalStorageDirectory();
    observer = new FileObserver(dir, FileObserver.CREATE | FileObserver.DELETE | FileObserver.MODIFY) {
      @Override
      public void onEvent(final int event, final String file) {
        Runnable runnable = new Runnable() {
          @Override
          public void run() {
            events.success(file);
            // events.success(event);
          }
        };
        mainHandler.post(runnable);
      }
    };
    System.out.println("watching started");
    observer.startWatching();
  }

  @Override
  public void onCancel(Object args) {

  }
}
