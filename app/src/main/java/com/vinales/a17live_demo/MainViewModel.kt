package com.vinales.a17live_demo

import Manager.Interface.IHttpManager
import Manager.ManagerImpl.HttpManagerImpl
import android.util.Log
import androidx.lifecycle.MutableLiveData

class MainViewModel {

    var httpManager : IHttpManager = HttpManagerImpl()

    var dataList : MutableLiveData<List<String>> = MutableLiveData<List<String>>()

    var editData : String = ""
        get() {
            return field
        }
        set(value) {
            field = value
        }

    fun onSeachBtnClick(){
        dataList.value = httpManager.getSearchResult(editData)
    }
}