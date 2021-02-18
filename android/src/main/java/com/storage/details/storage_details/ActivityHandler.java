package com.storage.details.storage_details;

import io.flutter.plugin.common.PluginRegistry;
import android.net.Uri;
import android.content.Intent;
import android.content.Context;

public class ActivityHandler implements PluginRegistry.ActivityResultListener {

  private Context applicationContext;

  public ActivityHandler(Context context) {
    this.applicationContext = context;
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent resultData) {
    if (requestCode == 10) {
      Uri uri = null;
      if (resultData != null) {
        uri = resultData.getData();
        System.out.println(uri.getPath());
        applicationContext.getContentResolver().takePersistableUriPermission(uri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        System.out.println("permissionGranted");
      }
      return true;
    } else {
      System.out.println("fails");
      return false;
    }
  }
}
