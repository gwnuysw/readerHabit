package com.example.readerhabit

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Book(
    @PrimaryKey var id: Long = 0,
    var title: String = "",
    var author: String = "",
    var publisher: String = "",
    var translator: String = "",
    var date: Long = 0
):RealmObject(){

}