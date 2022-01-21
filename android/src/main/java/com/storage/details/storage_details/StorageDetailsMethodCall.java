package com.storage.details.storage_details;

import android.os.Looper;
import android.os.Handler;
//import android.os.Looper;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Handler;
//import java.util.logging.LogRecord;

public class StorageDetailsMethodCall implements MethodChannel.MethodCallHandler {
    private Methods methods;

    public StorageDetailsMethodCall(Methods methods) {
        this.methods = methods;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {

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
                final ArrayList<HashMap<String, String>> space = methods.getSpace();
                result.success(space);
                return;
            case "getVideos":

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
////                            result.success(apps);
////                            final ArrayList videos;
                            methods.getVideos(new ThreadResult() {
                                @Override
                                public void onResult(ArrayList<HashMap<String, Object>> value) {
                                    result.success(value);

                                }
                            });
//                            result.success(videos);
//                        }
//                    });
//                    videos = methods.getVideos();
//                    result.success(videos);
                }


                return;
            default:
                result.notImplemented();
                return;
        }
    }
}
