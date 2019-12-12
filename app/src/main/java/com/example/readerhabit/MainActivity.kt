package com.example.readerhabit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.widget.AdapterView
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_book.view.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemLongClickListener{
    private val REQUEST_CAMERA = 1000
    private val REQUEST_STORE = 2000
    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {

        startActivity<EditActivity>("id" to view?.text6?.text.toString().toLong())
        return true
    }

    val realm = Realm.getDefaultInstance()
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val realmResult = realm.where<Book>().findAll().sort("date", Sort.DESCENDING)
        Log.d("check",realmResult.size.toString())
        if(realmResult.size > 0){
            imageView.visibility = INVISIBLE
            noitemText.visibility = INVISIBLE
        }
        val adapter = BookListAdapter(realmResult)
        listView.adapter = adapter

        realmResult.addChangeListener { _->adapter.notifyDataSetChanged() }

        listView.onItemLongClickListener = this

        listView.setOnItemClickListener { parent, view, position, id ->
            /*
            * text6에 있는 아이디를 다이어리 인텐트에 넘겨준다.
            * */
            startActivity<diary>("id" to view.text6.text.toString().toLong())
        }
        newDiary.setOnClickListener { view ->
            startActivity<EditActivity>()

//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show()
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                alert("책내용을 사진으로 찍으면 타자를 안쳐도됩니다."){
                    yesButton {
                        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
                    }
                    noButton {  }
                }.show()
            }else{
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
            }
        }else{

        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                alert("파일 쓰기 권한을 허용해 주세요 사진 저장할 겁니다"){
                    yesButton {
                        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORE)
                    }
                    noButton {  }
                }.show()
            }else{
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORE)
            }
        }else{

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}