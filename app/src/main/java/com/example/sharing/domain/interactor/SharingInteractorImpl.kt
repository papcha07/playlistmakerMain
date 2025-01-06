package com.example.sharing.domain.interactor

import com.example.sharing.data.SharingRepository
import com.example.sharing.domain.api.SharingInteractorInterface
import com.example.sharing.domain.model.ShareDataInfo
import com.example.sharing.domain.repository.SharingRepositoryInterface

class SharingInteractorImpl(val sharingRepository: SharingRepositoryInterface) : SharingInteractorInterface{

    override fun shareLink(link: String) {
        sharingRepository.shareLink(link)
    }

    override fun openLink(link: String) {
        sharingRepository.openLink(link)
    }

    override fun messageSupport(data: ShareDataInfo) {
        sharingRepository.messageSupport(data)
    }
}