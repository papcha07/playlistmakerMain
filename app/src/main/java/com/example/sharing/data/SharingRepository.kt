package com.example.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.sharing.domain.repository.SharingRepositoryInterface
import com.example.sharing.domain.model.ShareDataInfo
import androidx.core.content.ContextCompat.startActivity
import androidx.room.ForeignKey
import com.example.media.domain.api.PlayList
import com.example.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharingRepository(val context: Context) : SharingRepositoryInterface {

    override fun shareLink(link: String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_TEXT, link)
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context,sendIntent,null)
    }

    override fun openLink(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, browserIntent, null)
    }

    override fun messageSupport(email: ShareDataInfo) {
        val reportIntent = Intent(Intent.ACTION_SEND)
        reportIntent.setType("message/rfc822")
        reportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.email))
        reportIntent.putExtra(Intent.EXTRA_SUBJECT, email.subject)
        reportIntent.putExtra(Intent.EXTRA_TEXT, email.text)
        reportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context,reportIntent,null)
    }

    override fun shareTracks(playList: PlayList) {
        var desc = playList.description
        if(desc == null){
            desc = ""
        }
        val typeobj = object : TypeToken<List<Track>>() {}.type
        val trackList: MutableList<Track> = Gson().fromJson(playList.trackList, typeobj) ?: mutableListOf()
        var text = "Название плейлиста: ${playList.name}\nОписание: ${desc}\n[${playList.trackCount}]\n"

        for(i in 0..trackList.size - 1){
            var stroka = "${i}. ${trackList[i].trackName} (${trackList[i].trackTimeMillis})\n"
            text += stroka
        }
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  // <- Добавьте эту строку!
        }
        val shareIntent = Intent.createChooser(sendIntent, "Поделиться через").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  // <- И эту, если используете Chooser!
        }
        startActivity(context, shareIntent, null)
    }


}