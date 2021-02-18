package com.storage.details.storage_details;

import android.content.Context;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import androidx.annotation.NonNull;
import java.io.File;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class StorageDetailsMethodCall implements MethodChannel.MethodCallHandler {
    private Methods methods;

    public StorageDetailsMethodCall(Methods methods) {
        this.methods = methods;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

        switch (call.method) {
            case "getPlatformVersion":
                final String buildVersion = "Android " + android.os.Build.VERSION.RELEASE;
                result.success(buildVersion);
                return;
            case "deleteWhenError":
                List<String> paths = call.argument("paths");
                boolean data = methods.deleteFileSystemEntity(paths);
                result.success(data);
                return;
            case "getStorageDetails":
                final ArrayList<HashMap> space = methods.getSpace();
                result.success(space);
                return;
            default:
                result.notImplemented();
                return;
        }
    }
}
