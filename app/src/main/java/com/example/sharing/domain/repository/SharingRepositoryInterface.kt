package com.example.sharing.domain.repository

import com.example.media.domain.api.PlayList
import com.example.search.domain.model.Track
import com.example.sharing.domain.model.ShareDataInfo

interface SharingRepositoryInterface {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun messageSupport(data: ShareDataInfo)
    fun shareTracks(playList: PlayList)
}