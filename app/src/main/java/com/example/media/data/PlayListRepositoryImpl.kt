package com.example.media.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
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
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlayListRepositoryImpl(
    private val db: TrackDataBase,
    private val playListDbConverter: PlaylistDbConverter,
    private val context: Context
) : PlayListRepository {


    private fun saveImageToPrivateStorage(contentUri: String?): String? {
        if (contentUri.isNullOrBlank()) return null

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
            Log.e("localeException", "Error in fromLocaleStorageToUri: ${e.stackTraceToString()}")
            null
        }
    }


    private fun fromLocaleStorageToUri(localPath: String?): String? {
        if (localPath.isNullOrBlank()) {
            Log.d("URI_CONVERSION", "Input path is null or blank")
            return null
        }

        return try {
            val file = File(localPath)
            if (!file.exists()) {
                Log.d("URI_CONVERSION", "File does not exist: $localPath")
                return localPath // Возвращаем оригинальный путь
            }

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(localPath, options)

            if (options.outWidth <= 0 || options.outHeight <= 0) {
                Log.d("URI_CONVERSION", "File is not a valid image: $localPath")
                return localPath // Возвращаем оригинальный путь
            }

            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            } else {
                Uri.fromFile(file)
            }

            Log.d("URI_CONVERSION", "Successfully converted to URI: ${uri.toString()}")
            uri.toString()
        } catch (e: Exception) {
            Log.e("URI_CONVERSION", "Error converting path to URI: ${e.message}", e)
            localPath
        }
    }


    override suspend fun addPlayList(playlist: PlayList) {
        withContext(Dispatchers.IO) {
            val convertertedPathToLocalStorage = saveImageToPrivateStorage(playlist.path)
            playlist.path = convertertedPathToLocalStorage
            val newPlaylist = playListDbConverter.map(playlist)
            db.playListDao().addPlaylist(newPlaylist)
        }
    }

    override fun getAllPlayLists(): Flow<List<PlayList>> = flow {
        val entityList = db.playListDao().getAllPlayLists()
        val convertedList = convertToPlayList(entityList)
        emit(convertedList)
    }

    override fun getCurrentPlayList(id: Int): Flow<PlayList> {
        return flow{
            val playListById = db.playListDao().getPlayListById(id)
            val convertedList = playListDbConverter.map(playListById)
            emit(convertedList)
        }
    }

    override suspend fun updatePlayList(playlist: PlayList) {
        db.playListDao().updatePlayList(playListDbConverter.map(playlist))
    }

    private fun convertToPlayList(list: List<PlaylistEntity>): List<PlayList> {
        return list.map { playlist ->
            val tempPath = fromLocaleStorageToUri(playlist.path)
            playlist.path = tempPath
            playListDbConverter.map(playlist)
        }
    }
}