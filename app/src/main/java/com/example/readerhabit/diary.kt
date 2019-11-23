package com.example.readerhabit

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_diary.*
import kotlinx.android.synthetic.main.content_diary.*
import kotlinx.android.synthetic.main.item_diary.*
import kotlinx.android.synthetic.main.item_diary.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class diary : AppCompatActivity() {
    val realm = Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)
        setSupportActionBar(toolbar)

        /*
        * 인텐트로 책 id를 받는다.
        * */
        val Bookid = intent.getLongExtra("id", -1L)
        val realmResult = realm.where<DiaryOfbook>().`in`("bookid", arrayOf(Bookid)).findAll().sort("date", Sort.DESCENDING)
        val adapter = DiaryListAdapter(realmResult)
        diaryList.adapter = adapter

        realmResult.addChangeListener{
            _ -> adapter.notifyDataSetChanged()
        }

        diaryList.setOnItemClickListener { parent, view, position, id ->
            startActivity<DiaryEditActivity>("Bookid" to Bookid,"id" to view.diarytext5.text.toString().toLong())
        }
        newDiary.setOnClickListener { view ->
            startActivity<DiaryEditActivity>("Bookid" to Bookid)
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
        }
    }
}
