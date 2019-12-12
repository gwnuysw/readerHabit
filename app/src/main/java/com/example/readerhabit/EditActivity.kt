package com.example.readerhabit

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.util.*

class EditActivity : AppCompatActivity() {

    val calender: Calendar = Calendar.getInstance()
    val realm = Realm.getDefaultInstance()

    private fun insertBook(){
        realm.beginTransaction()

        val newItem = realm.createObject<Book>(nextId())

        newItem.title = Title.text.toString()
        newItem.author = Author.text.toString()
        newItem.publisher = Publisher.text.toString()
        if(Translator.text.toString().equals("")){

        }
        else{
            newItem.translator = Translator.text.toString()
        }
        newItem.date = calender.timeInMillis

        realm.commitTransaction()
        alert("좋아요 같이 책을 읽어봐요!"){
            yesButton{finish()}
        }.show()
    }

    private fun deleteBook(id:Long){
        realm.beginTransaction()
        val deleteItem = realm.where<Book>().equalTo("id", id).findFirst()!!
        deleteItem.deleteFromRealm()
        realm.commitTransaction()
        alert("내용이 삭제되었습니다."){
            yesButton{finish()}
        }.show()
    }

    private fun nextId():Int{
        val maxId = realm.where<Book>().max("id")
        if(maxId != null){
            return maxId.toInt() + 1
        }
        return 0
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val id = intent.getLongExtra("id", -1L)
        if(id == -1L){
            deleteFab.visibility = View.GONE
            insertMode()
        }else{
            doneFab.visibility = View.VISIBLE
            updateMode(id)
        }
    }
    @SuppressLint("RestrictedApi")
    private fun insertMode(){

        doneFab.setOnClickListener {
            if(Title.text.toString().equals("") || Author.text.toString().equals("") || Publisher.text.toString().equals("")){
                alert("책 제목, 글쓴이, 출판사를 꼭 써주세요 "){
                    yesButton {  }
                }.show()
            }
            else{
                insertBook()
            }
        }
    }
    private fun updateBook(id:Long){
        realm.beginTransaction()
        val updateItem = realm.where<Book>().equalTo("id", id).findFirst()!!
        updateItem.title = Title.text.toString()
        updateItem.author = Author.text.toString()
        updateItem.publisher = Publisher.text.toString()
        updateItem.translator = Translator.text.toString()
        realm.commitTransaction()
        alert("내용이 변경 되었습니다."){
            yesButton { finish() }
        }.show()
    }
    private fun updateMode(id:Long){
        val BookData = realm.where<Book>().equalTo("id",id).findFirst()!!
        val DiaryData = realm.where<DiaryOfbook>().equalTo("bookid",id).findAll()
        toast(DiaryData.toString())
        Title.setText(BookData.title)
        Author.setText(BookData.author)
        Publisher.setText(BookData.publisher)
        Translator.setText(BookData.translator)

        doneFab.setOnClickListener {
            updateBook(id)
        }
        deleteFab.setOnClickListener {
            if(DiaryData.size > 0){
                alert("다이어리를 먼저 지워주세요"){
                    yesButton { finish() }
                }.show()
            }
            else{
                deleteBook(id)
            }
        }
    }
    override fun onDestroy(){
        super.onDestroy()
        realm.close()
    }
}
