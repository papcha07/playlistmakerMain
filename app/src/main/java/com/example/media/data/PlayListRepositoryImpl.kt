package com.example.media.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.room.util.foreignKeyCheck
import com.example.media.db.PlaylistEntity
import com.example.media.db.TrackDataBase
import com.example.media.domain.api.PlayList
import com.example.media.domain.api.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlayListRepositoryImpl(
    private val db: TrackDataBase,
    private val playListDbConverter : PlaylistDbConverter,
    private val context : Context
) : PlayListRepository {



    private fun saveImageToPrivateStorage(contentUri: String): String {
        return try {
            val uri = contentUri.toUri()
            val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum").apply {
                if (!exists()) mkdirs()
            }

            val fileName = "playlist_${System.currentTimeMillis()}.jpg"
            val outputFile = File(filePath, fileName)

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(outputFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            outputFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private fun fromLocaleStorageToUri(localPath: String): String {
        return try {
            val file = File(localPath)
            if (!file.exists()) return ""

            val bitmap = BitmapFactory.decodeFile(localPath)

            val tempFile = File.createTempFile("temp_img_", ".jpg", context.cacheDir)
            FileOutputStream(tempFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }

            Uri.fromFile(tempFile).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }


    override suspend fun addPlayList(playlist: PlayList) {
        with(Dispatchers.IO){
            val convertertedPathToLocalStorage = saveImageToPrivateStorage(playlist.path)
            playlist.path = convertertedPathToLocalStorage
            val newPlaylist = playListDbConverter.map(playlist)
            db.playListDao().addPlaylist(newPlaylist)
        }
    }

    override fun getAllPlayLists(): Flow<List<PlayList>> = flow{
        val entityList = db.playListDao().getAllPlayLists()
        val convertedList = convertToPlayList(entityList)
        emit(convertedList)
    }

    private fun convertToPlayList(list: List<PlaylistEntity>) : List<PlayList>{
        return list.map {
            track ->
            val tempPath = fromLocaleStorageToUri(track.path)
            track.path = tempPath
            playListDbConverter.map(track)
        }
    }
}