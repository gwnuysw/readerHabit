package com.example.readerhabit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import org.jetbrains.anko.find
import android.widget.Toast
import android.widget.AdapterView



class BookListAdapter(realmResult: OrderedRealmCollection<Book>):RealmBaseAdapter<Book>(realmResult){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vh: ViewHolder
        val view: View
        if(convertView == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_book,parent, false)

            vh = ViewHolder(view)
            view.tag = vh
        }else{
            view = convertView
            vh = view.tag as ViewHolder
        }
        /*
        //여기서 리스트뷰에 있는 아이템의 문자열 값을 지정해 준다.
        */
        if(adapterData != null){
            val item = adapterData!![position]
            vh.dateTextView.text = android.text.format.DateFormat.format("yyyy/MM/dd", item.date)
            vh.titleTextView.text = item.title
            vh.authorTextView.text = item.author
            vh.publisherTextView.text = item.publisher
            vh.translatorTextView.text = item.translator
            vh.id.text = item.id.toString()
        }
        return view
    }

    override fun getItemId(position: Int): Long{
        if(adapterData != null){
            return adapterData!![position].id
        }
        return super.getItemId(position)
    }
}
class ViewHolder(view: View){

    val dateTextView: TextView = view.find(R.id.text1)
    val titleTextView: TextView = view.find(R.id.text2)
    val authorTextView: TextView = view.find(R.id.text3)
    val publisherTextView: TextView = view.find(R.id.text4)
    val translatorTextView: TextView = view.find(R.id.text5)
    val id: TextView = view.find(R.id.text6)
}