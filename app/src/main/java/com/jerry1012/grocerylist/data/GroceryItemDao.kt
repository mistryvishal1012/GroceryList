package com.jerry1012.grocerylist.data

import android.provider.LiveFolders
import androidx.lifecycle.LiveData
import androidx.room.*
import com.jerry1012.grocerylist.model.GroceryItem

@Dao
interface GroceryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(groceryItem: GroceryItem)

    @Query("SELECT * FROM Grocery_Table ORDER BY id ASC")
    fun readAllItems() : LiveData<List<GroceryItem>>

    @Update
    suspend fun updateItem(groceryItem: GroceryItem)

    @Delete
    suspend fun deleteItem(groceryItem: GroceryItem)

    @Query("DELETE FROM Grocery_Table")
    suspend fun deleteAllItem()

    @Query("SELECT * FROM GROCERY_TABLE ORDER BY ItemName ASC")
    fun getResultByItem() : LiveData<List<GroceryItem>>

    @Query("SELECT * FROM GROCERY_TABLE ORDER BY SuperMarket ASC")
    fun getResultBySuperMarketItem() : LiveData<List<GroceryItem>>

    @Query("SELECT * FROM GROCERY_TABLE ORDER BY day,hour,min,sec,month,year ASC")
    fun getResultByDateItem() : LiveData<List<GroceryItem>>

    @Query("SELECT * FROM GROCERY_TABLE WHERE ItemName LIKE :searchquery OR SuperMarket LIKE :searchquery ORDER BY ItemName ASC")
    fun getResultBySearchItem(searchquery:String) : LiveData<List<GroceryItem>>

}