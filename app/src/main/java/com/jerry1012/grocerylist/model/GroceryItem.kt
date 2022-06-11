package com.jerry1012.grocerylist.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Grocery_Table")
data class GroceryItem (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val ItemName:String,
    val SuperMarket:String,
    val NumberOfItem:Float,
    @Embedded
    val date: DateFormat
)
data class DateFormat(
    val day:Int,
    val month:Int,
    val year:Int,
    val hour:Int,
    val min:Int,
    val sec:Int
)
