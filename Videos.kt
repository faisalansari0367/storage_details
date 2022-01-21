 val projection = arrayOf(
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
    private fun getVideoList(projection: Array<String>) {

        val videos: MutableList<HashMap<String, Any>> = ArrayList()
        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndexData = cursor!!.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA);
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
                val name = cursor.getString(nameColumn)
                val imagePath = cursor.getString(columnIndexData)
                val size = cursor.getString(sizeColumn)
                val duration = cursor.getString(durationColumn)
                val resolution = cursor.getString(resolution)
                val date = cursor.getString(dateTaken)



                val contentUri: Uri = ContentUris.withAppendedId(uri, id)
                val thumbnail = this.contentResolver.loadThumbnail(contentUri, Size(640, 480), null)
                val byteArray = getByteArrayFromBitmap(thumbnail)
                val map = HashMap<String, Any>()
                map["name"] = name
                map["imagePath"] = imagePath
                map["size"] = size
                map["duration"] = duration
                map["duration"] = duration
                map["resolution"] = resolution
                map["resolution"] = resolution
                map["date"] = date
                map["thumbnail"] = byteArray


                videos.add(map)
//                Log.e("all path", absolutePathOfImage)
            }
            cursor.close()
        }
        Log.e("Videos", videos.toString())
    }

    private fun getByteArrayFromBitmap(bitmap: Bitmap) : ByteArray {
        val bmp: Bitmap = bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        bmp.recycle()
        return byteArray
    }
