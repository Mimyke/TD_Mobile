package com.doublea.td2.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doublea.td2.network.UserInfo;
import com.doublea.td2.network.UserInfoRepository;
import kotlinx.coroutines.launch

import okhttp3.MultipartBody;

class UserInfoViewModel: ViewModel() {
    private val _userInfo = MutableLiveData<UserInfo>()
    public val userInfo: LiveData<UserInfo> = _userInfo
    private val userInfoRepository = UserInfoRepository()

    fun getInfo() {
        viewModelScope.launch {
            userInfoRepository.getInfo()?.let { newUserInfo ->
                _userInfo.value = newUserInfo
            }
        }
    }

    fun updateAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch{
            userInfoRepository.updateAvatar(avatar)?.let { newUserInfo ->
                _userInfo.value = newUserInfo
            }
        }
    }

    fun update(newUserInfo: UserInfo) {
        viewModelScope.launch {
            userInfoRepository.update(newUserInfo)?.let { newUserInfo ->
                _userInfo.value = newUserInfo
            }
        }
    }
}