package com.ezstudio.playyyyyy

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.icu.text.DecimalFormat
import android.net.Uri
import android.provider.MediaStore
import com.ezstudio.playyyyyy.models.AlbumModel
import com.ezstudio.playyyyyy.models.AristModel
import com.ezstudio.playyyyyy.models.TrackModel


class AlbumArt {

    companion object {
         fun getAudioByAlbumId(
             context: Context,
             albumId: Long? = null,
             artistId: Long? = null

        ): ArrayList<TrackModel> {
            val tempaudiolist: ArrayList<TrackModel> = ArrayList()
            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            var selection = ""
            albumId?.let {
                selection = MediaStore.Audio.Media.ALBUM_ID + "== $albumId"
            }
            artistId?.let {
                selection = MediaStore.Audio.Media.ARTIST_ID + "== $artistId"
            }
            

            val cursor: Cursor? = context.contentResolver.query(uri, null, selection, null, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val title =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val path =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val artist =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    var timemusic =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    var duration =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val idAlbum =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val yearMusic =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))
                    val sortMusic =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                    val idArtist =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))
                    val size: String =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                    val album: String =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val type: String =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE))
                    val yearRealease:Int = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR))

                    // var f:Float = size/1000f
                    //  val sizee:Int = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                    timemusic = milisecondtoTimer(timemusic.toLong())
                    val trackModel =
                        TrackModel(
                            path,
                            title,
                            artist,
                           timemusic,
                            getAlbumartURI(idAlbum),
                            yearMusic,
                            sortMusic,
                            idAlbum,
                            idArtist,
                            readableFileSize(size.toLong()),
                            album,
                            type,
                            duration,
                            yearRealease

                        )
                    tempaudiolist.add(trackModel)
                }
                cursor.close()
            }
            return tempaudiolist
        }



        fun getAllAlbum(
            context: Context,
            artistId: Long? = null,
            albumId: Long? = null
        ): ArrayList<AlbumModel> {
            val tempAlbumList: ArrayList<AlbumModel> = ArrayList()
            val uriAlbum: Uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

            var selection = ""
            artistId?.let {
                selection = MediaStore.Audio.Albums.ARTIST_ID + "== $artistId"
            }
            albumId?.let {
                selection = MediaStore.Audio.Albums.ALBUM_ID + "== $albumId"
            }
            val cursorAlbum: Cursor? =
                context.contentResolver.query(uriAlbum, null, selection, null, null)
            if (cursorAlbum != null) {
                while (cursorAlbum.moveToNext()) {
                    val tileAbum: String =
                        cursorAlbum.getString(cursorAlbum.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM))
                    val artistAbum: String =
                        cursorAlbum.getString(cursorAlbum.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST))
                    val idAlbum: Long =
                        cursorAlbum.getLong(cursorAlbum.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID))
                    val yearRelease =
                        cursorAlbum.getInt(cursorAlbum.getColumnIndexOrThrow(MediaStore.Audio.Albums.LAST_YEAR))
                    val artist_Id:Long =  cursorAlbum.getLong(cursorAlbum.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST_ID))

                    val albumModel =
                        AlbumModel(
                            idAlbum.toLong(),
                            tileAbum,
                            artistAbum,
                            getAlbumartURI(idAlbum),
                            yearRelease,
                            artist_Id
                        )
                    tempAlbumList.add(albumModel)
                }
                cursorAlbum.close()
            }
            return tempAlbumList
        }
//        fun getAlbumart(context: Context,album_id: Long?): Bitmap? {
//            var bm: Bitmap? = null
//            try {
//                val sArtworkUri = Uri
//                    .parse("content://media/external/audio/albumart")
//                val uri = ContentUris.withAppendedId(sArtworkUri, album_id!!)
//                val pfd: ParcelFileDescriptor = .getContentResolver()
//                    .openFileDescriptor(uri, "r")
//                if (pfd != null) {
//                    val fd: FileDescriptor = pfd.fileDescriptor
//                    bm = BitmapFactory.decodeFileDescriptor(fd)
//                }
//            } catch (e: Exception) {
//            }
//            return bm
//        }

        //        fun sizeMusic(size: Int): String? {
//            var hrSize = ""
//            val m = size / 1024.0
//            val dec = java.text.DecimalFormat("0.00")
//            hrSize = if (m > 1) {
//                dec.format(m).concat(" MB")
//            } else {
//                dec.format(size).(" KB")
//            }
//            return hrSize
//        }
        fun readableFileSize(size: Long): String? {
            if (size <= 0) return "0"
            val units = arrayOf("B", "kB", "MB", "GB", "TB")
            val digitGroups =
                (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#")
                .format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
        }

        fun getAlbumartURI(album_id: Long): Uri? {
            return ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                album_id.toLong()
            )
        }


        fun getAllArtist(context: Context, idArtist: Long? = null): ArrayList<AristModel> {
            val tempartist: ArrayList<AristModel> = ArrayList()
            val uriartist: Uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
            var selection = ""
            idArtist?.let {
                selection = MediaStore.Audio.Artists._ID + "= $idArtist"
            }
            val cursorartist: Cursor? =
                context.contentResolver.query(uriartist, null, selection, null, null)

            if (cursorartist != null) {
                while (cursorartist.moveToNext()) {
                    val trackNumber: Int =
                        cursorartist.getInt(cursorartist.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS))
                    val albumNumber: Int =
                        cursorartist.getInt(cursorartist.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS))
                    val titleArtist: String =
                        cursorartist.getString(cursorartist.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.ARTIST))
                    val idArtist =
                        cursorartist.getLong(cursorartist.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID))

                    val aristModel =
                        AristModel(
                            idArtist,
                            getArtistArtURI(context, idArtist).toString(),
                            titleArtist,
                            albumNumber,
                            trackNumber
                        )
                    tempartist.add(aristModel)
                }
                cursorartist.close()
            }
            return tempartist
        }
//        fun getAlbumArtUri(context: Context,idAlbum: Int):Uri?{
//            val selection = MediaStore.Audio.AlbumColumns.ALBUM_ID + "==" + idAlbum
//            val cursor = context.contentResolver.query(
//                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//                null, selection, null, null
//            )
//            if (cursor == null || !cursor.moveToFirst()) {
//                return null
//            }
//            val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID))
//            return getAlbumartURI(id)
//        }

        fun getArtistArtURI(context: Context, idAlbum: Long): String? {
            val selection = MediaStore.Audio.Media.ARTIST_ID + "==" + idAlbum
            val cursor = context.contentResolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                null, selection, null, null
            )
            if (cursor == null || !cursor.moveToFirst()) {
                return ""
            }
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID))
            return getAlbumartURI(id).toString()
        }

//        fun getAlbumArt(uri: String): ByteArray? {
//            val retriever = MediaMetadataRetriever()
//            retriever.setDataSource(uri)
//            val art = retriever.embeddedPicture
//            retriever.release()
//            art.let {
//                return it
//            }
//        }

//        fun getAlbumArt(uri:String):Uri?{
//            val retriever = MediaMetadataRetriever()
//            retriever.setDataSource(uri);
//
//            try {
//                val art = retriever.embeddedPicture
//
//                val songImage:Bitmap = BitmapFactory
//                        .decodeByteArray(art, 0, art.size);
//
//                album_art.setImageBitmap(songImage);
//
//            } catch (Exception e) {
//                album_art.setBackgroundColor(Color.GRAY);
//            }
//        }
//
//    }

        fun milisecondtoTimer(mili: Long): String {
            var timestring: String = ""
            var secondstring: String = ""
            var minute: Int = ((mili % (1000 * 60 * 60)) / (1000 * 60)).toInt()
            var second: Int = ((mili % (1000 * 60 * 60)) % (1000 * 60) / 1000).toInt()
            if (second < 10) {
                secondstring = "0" + second
            } else {
                secondstring = "" + second
            }
            timestring = "" + minute + ":" + secondstring
            return timestring
        }
    }


}