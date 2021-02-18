package com.storage.details.storage_details;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.BinaryMessenger;

import android.content.ActivityNotFoundException;
import android.app.Activity;
import android.content.Context;


/** StorageDetailsPlugin */
public class StorageDetailsPlugin implements FlutterPlugin, ActivityAware {

  private MethodChannel methodChannel;
  private EventChannel eventChannel;

  private Context applicationContext;
  private Activity activity;

  private EventHandler eventHandler;
  private MethodCallHandler methodCallHandler;
  private Methods methods;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final StorageDetailsPlugin instance = new StorageDetailsPlugin();
    instance.onAttachedToEngine(registrar.context(), registrar.messenger());
    final ActivityHandler activityHandler = new ActivityHandler(registrar.context());
    registrar.addActivityResultListener(activityHandler);
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
    methods.setActivity(activity);

  }

  @Override
  public void onDetachedFromActivity() {
    this.activity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    onAttachedToActivity(binding);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity();
  }

  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    onAttachedToEngine(binding.getApplicationContext(), binding.getBinaryMessenger());
  }

  private void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger) {
    this.applicationContext = applicationContext;
    methodChannel = new MethodChannel(messenger, "storage_details");
    eventChannel = new EventChannel(messenger, "event_channel");
    // eventChannel
    eventHandler = new EventHandler(applicationContext);
    eventChannel.setStreamHandler(eventHandler);
    // method channel
    methods = new Methods(applicationContext);
    methodCallHandler = new StorageDetailsMethodCall(methods);
    methodChannel.setMethodCallHandler(methodCallHandler);
  }

  @Override
  public void onDetachedFromEngine(FlutterPluginBinding binding) {
    applicationContext = null;
    methodChannel = null;
    eventChannel = null;
    eventHandler = null;
    methodCallHandler = null;
    methodChannel.setMethodCallHandler(null);
    eventChannel.setStreamHandler(null);
  }
}
