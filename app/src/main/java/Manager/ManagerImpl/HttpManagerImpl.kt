package Manager.ManagerImpl

import Manager.Interface.IHttpManager
import android.util.Log

class HttpManagerImpl : IHttpManager {

    override fun getSearchResult(editDataInfo : String) : MutableList<String> {
        Log.d("Search Edit Key word",editDataInfo)

        //mock 搜尋資料結果
        var dataList = mutableListOf<String>()
        for(i in 1..37){
            dataList.add("$i-王小明")
        }
        return dataList
    }
}