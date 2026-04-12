package com.vyn.player.data.local

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.vyn.player.data.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaStoreDataSource(
    private val context: Context,
) {
    suspend fun getSongs(): List<Song> = withContext(Dispatchers.IO) {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
        )
        val selection = "${MediaStore.Audio.Media.SIZE} > 0 AND ${MediaStore.Audio.Media.DURATION} > 10000"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val songs = mutableListOf<Song>()

        context.contentResolver.query(
            collection,
            projection,
            selection,
            null,
            sortOrder,
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn).orEmpty()
                val artist = cursor.getString(artistColumn).orEmpty()
                val durationMillis = cursor.getLong(durationColumn)
                val contentUri = ContentUris.withAppendedId(
                    collection,
                    id,
                )

                songs += Song(
                    id = id,
                    title = title,
                    artistName = artist,
                    albumName = "",
                    uri = contentUri.toString(),
                    durationMillis = durationMillis,
                )
            }
        }

        Log.d("MediaStoreDebug", "Songs found: ${songs.size}")
        songs.take(5).forEachIndexed { index, song ->
            Log.d(
                "MediaStoreDebug",
                "Song ${index + 1}: title=${song.title}, uri=${song.uri}",
            )
        }

        songs
    }
}
