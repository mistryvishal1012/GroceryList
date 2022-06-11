package com.jerry1012.grocerylist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jerry1012.grocerylist.model.GroceryItem

@Database(entities = [GroceryItem::class],version = 2,exportSchema = false)
abstract class GroceryItemDatabase: RoomDatabase() {
    abstract fun groceryItemDao(): GroceryItemDao
    companion object{
        @Volatile
        private var INSTANCE: GroceryItemDatabase?=null
        fun getDatabase(context: Context) : GroceryItemDatabase {
            val tempInstance = INSTANCE
            if(INSTANCE !=null){
                return tempInstance!!
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GroceryItemDatabase::class.java,
                    "Grocery_Database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}