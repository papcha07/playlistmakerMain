package com.example.sharing.domain.api

import com.example.media.domain.api.PlayList
import com.example.sharing.domain.model.ShareDataInfo

interface SharingInteractorInterface {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun messageSupport(data: ShareDataInfo)
    fun shareTrack(playList: PlayList)
}