package com.crawford.ciq.dev

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crawford.ciq.dev.api.ApiHelper
import com.crawford.ciq.dev.repository.CiQRepository
import com.crawford.ciq.dev.ui.claimdetail.ClaimDetailViewModel
import com.crawford.ciq.dev.ui.home.HomeViewModel
import com.crawford.ciq.dev.ui.splash.SplashViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(CiQRepository(apiHelper)) as T
        }
        if(modelClass.isAssignableFrom(ClaimDetailViewModel::class.java)){
            return ClaimDetailViewModel(CiQRepository(apiHelper)) as T
        }

        if(modelClass.isAssignableFrom(SplashViewModel::class.java)){
            return SplashViewModel(CiQRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}