package com.storage.details.storage_details

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.util.*

class Videos(val context: Context) {


//    val videoList: ArrayList<HashMap<String, Any>>

    //
//    public Videos(c: Context ) {
//        this.context = c
//    }
    private val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.TITLE,
        MediaStore.Video.VideoColumns.DATA,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.RESOLUTION,
        MediaStore.Video.Media.SIZE,
    )





    @RequiresApi(Build.VERSION_CODES.Q)
    fun getVideoList(): MutableList<HashMap<String, Any>> {


            val videos: MutableList<HashMap<String, Any>> = ArrayList()


            val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            val columnIndexData =
                cursor!!.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA);
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val resolution = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)
            val dateTaken = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
            val displayName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)


            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val folderName = cursor.getString(nameColumn)
                    val imagePath = cursor.getString(columnIndexData)
                    val size = cursor.getString(sizeColumn)
                    val duration = cursor.getString(durationColumn)
                    val resolution = cursor.getString(resolution)
                    val date = cursor.getString(dateTaken)
                    val name = cursor.getString(displayName)
                    val contentUri: Uri = ContentUris.withAppendedId(uri, id)
                    val thumbnail =
                        context.contentResolver.loadThumbnail(contentUri, Size(640, 480), null)
                    val byteArray = getByteArrayFromBitmap(thumbnail)

                    val map = HashMap<String, Any>()
                    map["name"] = name
                    map["imagePath"] = imagePath
                    map["size"] = size
                    map["duration"] = duration
                    map["resolution"] = resolution
                    map["date"] = date
                    map["thumbnail"] = byteArray
                    map["folderName"] = folderName

                    videos.add(map)

                }
                cursor.close()
            }
            Log.e("Videos", videos.toString())
            println(videos)
           return videos
//        }

//       return videos.getCompleted();
//        return videos;

    }

    private fun getByteArrayFromBitmap(bitmap: Bitmap) : ByteArray {
        val bmp: Bitmap = bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        bmp.recycle()
        return byteArray
    }
}