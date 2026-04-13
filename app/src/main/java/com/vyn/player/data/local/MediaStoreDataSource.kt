package com.vyn.player.data.local

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.vyn.player.data.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaStoreDataSource(
    private val context: Context,
) {
    suspend fun getSongs(): List<Song> = withContext(Dispatchers.IO) {
        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
        )
        val selection = "${MediaStore.Audio.Media.SIZE} > 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val songs = mutableListOf<Song>()

        Log.d("CHECK_FLOW", "MediaStore query started")

        context.contentResolver.query(
            collection,
            projection,
            selection,
            null,
            sortOrder,
        )?.use { cursor ->
            Log.d("CHECK_FLOW", "Cursor count: ${cursor.count}")
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

                if (songs.size <= 5) {
                    Log.d(
                        "CHECK_FLOW",
                        "Song: ${title} | ${contentUri}",
                    )
                }
            }
        }

        songs
    }
}
