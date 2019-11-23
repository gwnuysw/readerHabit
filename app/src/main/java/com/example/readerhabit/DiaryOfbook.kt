package com.example.readerhabit

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DiaryOfbook(
    @PrimaryKey var id: Long = 0,
    var pagenumber: String = "",
    var describe: String = "",
    var bookid: Long = 0,
    var date: Long = 0
):RealmObject(){

}