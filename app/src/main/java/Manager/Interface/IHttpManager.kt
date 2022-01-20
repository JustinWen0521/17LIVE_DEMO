package Manager.Interface

interface IHttpManager {

    /**呼叫API 搜尋會員資料
     * */
    fun getSearchResult(editDataInfo : String): MutableList<String>
}