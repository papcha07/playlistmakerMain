package com.example.sharing.domain.repository

import com.example.sharing.domain.model.ShareDataInfo

interface SharingRepositoryInterface {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun messageSupport(data: ShareDataInfo)
}