package com.example.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.sharing.domain.repository.SharingRepositoryInterface
import com.example.sharing.domain.model.ShareDataInfo
import androidx.core.content.ContextCompat.startActivity

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


}