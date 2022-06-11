package com.jerry1012.grocerylist.ViewModel.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Delete
import androidx.room.Query
import com.jerry1012.grocerylist.data.GroceryItemDao
import com.jerry1012.grocerylist.model.GroceryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroceryItemRepository(private val groceryItemDao: GroceryItemDao) {
    val readAllItems:LiveData<List<GroceryItem>> = groceryItemDao.readAllItems()

    suspend fun updateItem(groceryItem: GroceryItem){
        groceryItemDao.updateItem(groceryItem)
    }

    suspend fun addItem(groceryItem: GroceryItem){
        groceryItemDao.addItem(groceryItem)
    }

    suspend fun deleteItem(groceryItem: GroceryItem){
        groceryItemDao.deleteItem(groceryItem)
    }


    suspend fun deleteAllItem(){
        groceryItemDao.deleteAllItem()
    }

    fun getResultByItem() : LiveData<List<GroceryItem>>{
        return groceryItemDao.getResultByItem() as LiveData<List<GroceryItem>>
    }

    fun getResultBySuperMarketItem() : LiveData<List<GroceryItem>>{
        return groceryItemDao.getResultBySuperMarketItem() as LiveData<List<GroceryItem>>
    }

    fun getResultByDateItem() : LiveData<List<GroceryItem>>{
        return groceryItemDao.getResultByDateItem() as LiveData<List<GroceryItem>>
    }

    fun getResultBySearchItem(searchquery:String) : LiveData<List<GroceryItem>>{
        return groceryItemDao.getResultBySearchItem(searchquery) as LiveData<List<GroceryItem>>
    }
}