package com.example.petslist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Dog(
    @SerializedName("id") @Expose val id: String,
    @SerializedName("url") @Expose val url: String,
    @SerializedName("breeds") @Expose val breeds: List<Any>,
    @SerializedName("categories") @Expose val categories: List<Any>
)