package com.example.readerhabit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import org.jetbrains.anko.find

class DiaryListAdapter(realmResult: OrderedRealmCollection<DiaryOfbook>):RealmBaseAdapter<DiaryOfbook>(realmResult){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vh: ViewHolder2
        val view:View

        if(convertView == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_diary, parent, false)

            vh = ViewHolder2(view)
            view.tag = vh
        }else{
            view = convertView
            vh = view.tag as ViewHolder2
        }

        if(adapterData != null){
            val item = adapterData!![position]
            vh.describeView.text = item.describe
            vh.dateView.text = android.text.format.DateFormat.format("yyyy/MM/dd", item.date)
            vh.pagenumberView.text = item.pagenumber
            vh.bookid.text = item.bookid.toString()
            vh.diaryid.text = item.id.toString()
        }
        return view
    }
}
class ViewHolder2(view: View){
    val dateView: TextView = view.find(R.id.diarytext1)
    val pagenumberView: TextView = view.find(R.id.diarytext2)
    val describeView: TextView = view.find(R.id.diarytext3)
    val bookid: TextView = view.find(R.id.diarytext4)
    val diaryid: TextView = view.find(R.id.diarytext5)
}