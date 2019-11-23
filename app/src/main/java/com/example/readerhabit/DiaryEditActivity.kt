package com.example.readerhabit

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_diary_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.util.*
import android.R.attr.bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer
import java.io.IOException


class DiaryEditActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance()
    val calendar:Calendar = Calendar.getInstance()
    val FROM_CAMERA = 1
    val FROM_GALLERY = 2
    val PICTURE_CROP = 3
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_edit)

        val Diaryid = intent.getLongExtra("id", -1L)
        val Bookid = intent.getLongExtra("Bookid", -1L)
        toast("afterPass BookId : "+Bookid.toString()+" DiaryId : "+Diaryid.toString())
        textFromPhoto()
        if(Diaryid == -1L){
            deleteFab2.visibility = View.GONE
            insertMode(Bookid)
        }else{
            updateMode(Diaryid)
        }

    }

    private fun textFromPhoto(){
        takePhoto.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, FROM_CAMERA)
                }
            }
        }
        selectPhoto.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, FROM_GALLERY)
            }
        }
    }
    @SuppressLint("SetTextI18n")
    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        var options = FirebaseVisionCloudDocumentRecognizerOptions.Builder()
            .setLanguageHints(Arrays.asList("ko"))
            .build();
        var textRecognizer = FirebaseVision.getInstance()
            .getCloudDocumentTextRecognizer(options);
        when (requestCode) {
            FROM_CAMERA ->{
                val imageBitmap = data!!.extras.get("data") as Bitmap
                var image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
                textRecognizer.processImage(image)
                    .addOnSuccessListener {
                        // Task completed successfully
                        // ...
                        Describe.setText(Describe.text.toString()+it.text.toString())
                    }
                    .addOnFailureListener {
                        // Task failed with an exception
                        // ...
                        toast(it.cause.toString())
                    }
            }
            FROM_GALLERY -> {
                var imageUri = data!!.getData();
                var imageStream = Uri.parse(imageUri.toString())
                val image: FirebaseVisionImage = FirebaseVisionImage.fromFilePath(this, imageUri)
                textRecognizer.processImage(image)
                    .addOnSuccessListener {
                        // Task completed successfully
                        // ...
                        Describe.setText(Describe.text.toString()+it.text.toString())
                    }
                    .addOnFailureListener {
                        // Task failed with an exception
                        // ...
                        toast(it.cause.toString())
                    }
            }
            PICTURE_CROP -> {

            }

        }

    }
    private fun insertMode(Bookid:Long){

        doneFab2.setOnClickListener {
            insertDiary(Bookid)
        }
    }
    private fun updateMode(Diaryid:Long){
        val DiaryData = realm.where<DiaryOfbook>().equalTo("id",Diaryid).findFirst()!!
        toast("update Mode BookId: "+DiaryData.bookid+" DiaryId: "+DiaryData.id)
        PageNumber.setText(DiaryData.pagenumber)
        Describe.setText(DiaryData.describe)
        doneFab2.setOnClickListener {
            updateDiary(DiaryData)
        }
        deleteFab2.setOnClickListener {
            deleteDiary(DiaryData)
        }
    }
    private fun insertDiary(Bookid: Long){
        realm.beginTransaction()

        val newDiary = realm.createObject<DiaryOfbook>(nextId())
        newDiary.pagenumber = PageNumber.text.toString()
        newDiary.describe = Describe.text.toString()
        newDiary.date = calendar.timeInMillis
        newDiary.bookid = Bookid

        realm.commitTransaction()
        toast("newDiary bookid : "+newDiary.bookid.toString()+" id : "+newDiary.id.toString())
        alert("교양이 하나 더 쌓였습니다."){
            yesButton{finish()}
        }.show()
    }
    private fun nextId():Int{
        val maxId = realm.where<DiaryOfbook>().max("id")
        if(maxId != null){
            return maxId.toInt()+1
        }
        return 0
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    private fun updateDiary(DiaryData:DiaryOfbook){
        realm.beginTransaction()

        val updateItem = DiaryData

        updateItem.pagenumber = PageNumber.text.toString()
        updateItem.describe = Describe.text.toString()

        realm.commitTransaction()

        alert("내용이 변경 되었어요!"){
            yesButton { finish() }
        }.show()
    }
    private fun deleteDiary(DiaryData: DiaryOfbook){
        realm.beginTransaction()
        val deleteItem = DiaryData

        deleteItem.deleteFromRealm()
        realm.commitTransaction()
        alert("내용이 삭제되었습니다."){
            yesButton { finish() }
        }.show()
    }
}
