package com.example.petslist.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fav_dogs_table")
data class Dog(
    @SerializedName("id") @Expose
    @PrimaryKey
    var id: String,
    @SerializedName("url") @Expose
    var url: String = ""
){
    @Ignore
    var liked:Boolean = false
}

