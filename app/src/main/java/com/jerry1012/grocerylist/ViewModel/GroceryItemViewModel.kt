package com.jerry1012.grocerylist.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jerry1012.grocerylist.ViewModel.Repository.GroceryItemRepository
import com.jerry1012.grocerylist.data.GroceryItemDatabase
import com.jerry1012.grocerylist.model.GroceryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroceryItemViewModel(application: Application) : AndroidViewModel(application) {
    var readAllItems : LiveData<List<GroceryItem>>
    private val repository : GroceryItemRepository
    init {
        val groceryItemDao = GroceryItemDatabase.getDatabase(application).groceryItemDao()
        repository = GroceryItemRepository(groceryItemDao)
        readAllItems = repository.readAllItems
    }

    fun addItem(groceryItem: GroceryItem) {
        viewModelScope.launch(Dispatchers.IO){
            repository.addItem(groceryItem)
        }
    }

     fun updateItem(groceryItem: GroceryItem) {
         viewModelScope.launch(Dispatchers.IO){
             repository.updateItem(groceryItem)
         }
     }


    fun deleteItem(groceryItem: GroceryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(groceryItem)
        }
    }


     fun deleteAllItem() {
         viewModelScope.launch(Dispatchers.IO) {
             repository.deleteAllItem()
         }
     }


    fun getResultByItem() : LiveData<List<GroceryItem>>{
        return   repository.getResultByItem()
    }

     fun getResultBySuperMarketItem() : LiveData<List<GroceryItem>>{
         return  repository.getResultBySuperMarketItem()
     }

    fun getResultByDateItem() : LiveData<List<GroceryItem>>{
        return repository.getResultByDateItem()
    }

     fun getResultBySearchItem(searchquery:String) : LiveData<List<GroceryItem>>{
         return  repository.getResultBySearchItem(searchquery)
     }

}