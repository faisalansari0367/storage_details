package com.storage.details.storage_details;

import androidx.annotation.RequiresApi;
import android.os.Build;

import io.flutter.plugin.common.MethodChannel.Result;

import android.app.Activity;
import android.os.Environment;
import android.os.StatFs;
import android.content.Context;

import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.util.function.Function;

public class Methods {
    private Context applicationContext;
    private Activity activity;
    private final AsyncWork asyncWork;

    public Methods(Context context) {
        this.applicationContext = context;
        this.asyncWork = new AsyncWork();
    }

    void setActivity(Activity activity) {
        this.activity = activity;
    }

    ArrayList<HashMap<String, String>> getSpace() {
        String path = Environment.getExternalStorageDirectory().getPath();
        final ArrayList<HashMap<String, String>> extRootPaths = new ArrayList<>();
        final ArrayList<HashMap<String, String>> removable = new ArrayList<>();
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

    public HashMap<String, String> getSpaceDetails(String path) {
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



    boolean deleteFileSystemEntity(List<String> list) {
        try {
          for (String path : list) {
            File file = new File(path);
            System.out.println(file.canWrite());
            if (file.canWrite() == false) {
              getPermissionForSdCard();
            }
            deleteDirectory(file);
          }
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    
      public void deleteDirectory(File file) {
        if (file.exists()) {
          if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
              if (files[i].isDirectory()) {
                deleteDirectory(files[i]);
              } else {
                files[i].delete();
              }
            }
          }
          file.delete();
        }
      }

      @RequiresApi(Build.VERSION_CODES.Q)
      public void getVideos(final ThreadResult callback) {
//        Videos videos = new Videos(applicationContext);
        asyncWork.run(
                new Runnable() {
                    @Override
                    public void run() {
                        Videos videos = new Videos(applicationContext);
                        ArrayList<HashMap<String, Object>> result = (ArrayList<HashMap<String, Object>>) videos.getVideoList();
                        callback.onResult(result);
                    }
                }
        );
//        ArrayList<HashMap<String, Object>> result = (ArrayList<HashMap<String, Object>>) videos.getVideoList();
//          System.out.println(result);
//        return result;
      }
    
      public void getPermissionForSdCard() {
        Uri uri = Uri.fromFile(new File("/storage/AAC4-4E2A"));
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    
        try {
          activity.startActivityForResult(intent, 10);
          System.out.println("activity starts");
    
        } catch (ActivityNotFoundException e) {
          System.out.println(e);
        }
      }
}
