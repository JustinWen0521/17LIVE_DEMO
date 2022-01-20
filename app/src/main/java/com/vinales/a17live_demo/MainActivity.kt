package com.vinales.a17live_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.vinales.a17live_demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding : ActivityMainBinding
    var viewModel : MainViewModel = MainViewModel()

    /**所以查詢資料list*/
    var itemDataList = mutableListOf<String>()
    /**目前顯示資料List*/
    var nowPageList = mutableListOf<String>()

    /**recyclerView & adapter*/
    lateinit var cycleView : RecyclerView
    lateinit var itemAdapter : ItemAdapter

    /**記錄目前頁數*/
    var nowPage = 0
    /**記錄所有頁數*/
    var totalPage = 0
    /**記錄資料量總數*/
    var totalCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        init()
    }

    /**Init All UI Setting
     * */
    fun init(){

        //create recyclerView & adapter
        cycleView = activityMainBinding.itemCyclerview
        cycleView.layoutManager = LinearLayoutManager(this)
        itemAdapter = ItemAdapter()
        itemAdapter.list = nowPageList
        cycleView.adapter = itemAdapter

        //observe 搜尋資料結果
        viewModel.dataList.observe(this,object : Observer<List<String>>{
            override fun onChanged(list : List<String>?) {
                if(list != null && list.size > 0){
                    nowPageList.clear()
                    itemDataList.clear()
                    itemDataList.addAll(list)
                    totalCount = list.size
                    totalPage = getTotalPageInfo(list.size)
                    nowPage = 1
                    var startCount = calculatePageStartCount(nowPage)
                    var entCount = calculatePageEndCount(nowPage,totalCount)
                    nowPageList.addAll(getPageDataList(startCount,entCount))
                    itemAdapter.notifyDataSetChanged()
                }
            }
        })

        //set 搜尋按鈕 onClick event
        activityMainBinding.searchBtn.setOnClickListener{
            viewModel.editData = activityMainBinding.editSeach.text.toString()
            viewModel.onSeachBtnClick()
        }

        //set 上一頁 onClick event
        activityMainBinding.upBtn.setOnClickListener {
            if(totalPage > 1 && nowPage > 1) {
                nowPageList.clear()
                nowPage --
                var startCount = calculatePageStartCount(nowPage)
                var entCount = calculatePageEndCount(nowPage,totalCount)
                nowPageList.addAll(getPageDataList(startCount,entCount))
                itemAdapter.notifyDataSetChanged()
            }
        }

        //set 下一頁 onClick event
        activityMainBinding.nextBtn.setOnClickListener {
            if(totalPage > nowPage){
                nowPageList.clear()
                nowPage ++
                var startCount = calculatePageStartCount(nowPage)
                var entCount = calculatePageEndCount(nowPage,totalCount)
                nowPageList.addAll(getPageDataList(startCount,entCount))
                itemAdapter.notifyDataSetChanged()
            }
        }
    }

    /**計算點擊資料顯示開始位置
     * @param upPageNumber = 第幾頁
     * @return 回傳從哪一筆資料開始
     * */
    fun calculatePageStartCount(upPageNumber : Int) : Int{
        var upStartCount = (upPageNumber * 10) - 10
        return upStartCount
    }

    /**計算點擊資料顯示結束位置
     * @param upPageNumber = 第幾頁
     * @param totalCount = 全部有幾筆資料
     * @return 回傳從哪一筆資料開始
     * */
    fun calculatePageEndCount(upPageNumber : Int,totalCount : Int) : Int{
        var upEndCount = (upPageNumber * 10) - 1
        if(upEndCount > (totalCount - 1)){
            upEndCount = (totalCount - 1)
        }
        return upEndCount
    }

    /**取得該頁要回傳的資料List
     * @param startCount = 從第幾筆資料開始
     * @param endCount = 從第幾筆資料結束
     * @return 回傳要顯示的資料list
     * */
    fun getPageDataList(startCount : Int,endCount : Int) : MutableList<String> {
        var newNowDataList = mutableListOf<String>()

        newNowDataList.addAll(itemDataList.slice(startCount..endCount))
        return newNowDataList
    }

    /**計算資料list總計要分幾頁顯示
     * @param count = 總資料筆數
     * */
    fun getTotalPageInfo(count : Int) : Int{
        var totalPageNumber = count / 10
        var remainCoun = count % 10
        if(remainCoun > 0){
            totalPageNumber ++
        }
        return totalPageNumber
    }

    class ItemAdapter : Adapter<ItemAdapter.ItemHolder>() {

        var list = kotlin.collections.mutableListOf<String>()

        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            lateinit var item : TextView;

            init{
                item = itemView.findViewById(R.id.itemview)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.nameitem,parent,false)
            return ItemHolder(view)
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.item.setText(list.get(position))
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }
}
