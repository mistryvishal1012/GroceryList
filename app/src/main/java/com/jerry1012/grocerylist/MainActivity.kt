package com.jerry1012.grocerylist

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jerry1012.grocerylist.Adapter.GroceryItemAdapter
import com.jerry1012.grocerylist.ViewModel.GroceryItemViewModel
import com.jerry1012.grocerylist.model.DateFormat
import com.jerry1012.grocerylist.model.GroceryItem
import kotlinx.android.synthetic.main.addgroceryitemdialog_layout.view.*
import java.time.LocalDateTime
import android.app.SearchManager
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener

class MainActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var groceryviewmodel: GroceryItemViewModel
    private lateinit var groceryItemAdapter : GroceryItemAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvGroceryItem = findViewById<RecyclerView>(R.id.rv_groceryitem)
        val fabaddgroceryitem = findViewById<FloatingActionButton>(R.id.fab_addgroceritem)
        val spnsortBy = findViewById<Spinner>(R.id.spn_sort)
        val sortBy = resources.getStringArray(R.array.Sort)
        groceryviewmodel = ViewModelProvider(this).get(GroceryItemViewModel::class.java)
        groceryItemAdapter = GroceryItemAdapter(groceryviewmodel,this@MainActivity)
        rvGroceryItem.adapter = groceryItemAdapter
        rvGroceryItem.layoutManager = LinearLayoutManager(this)
        groceryviewmodel.readAllItems.observe(this,
                Observer {
                    groceryItemAdapter.setData(it)
                })
        val spnadapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, sortBy)
        spnadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spnsortBy.adapter = spnadapter
        spnsortBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                groceryviewmodel.readAllItems.observe(this@MainActivity,
                        Observer { groceryItemAdapter.setData(it) })
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (sortBy[position]) {
                    "Item Name" -> {
                        groceryviewmodel.getResultByItem().observe(this@MainActivity,
                                Observer {
                                    groceryItemAdapter.setData(it)
                                })
                        spnsortBy.setSelection(sortBy.indexOf("Item Name"))
                    }
                    "Super Market" -> {
                        groceryviewmodel.getResultBySuperMarketItem().observe(this@MainActivity,
                                Observer {
                                    groceryItemAdapter.setData(it)
                                })
                        spnsortBy.setSelection(sortBy.indexOf("Super Market"))
                    }
                    "Date" -> {
                        groceryviewmodel.getResultByDateItem().observe(this@MainActivity,
                                Observer {
                                    groceryItemAdapter.setData(it)
                                })
                        spnsortBy.setSelection(sortBy.indexOf("Date"))
                    }

                }
            }
        }
        fabaddgroceryitem.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.addgroceryitemdialog_layout,null)
            val currentDateTime = LocalDateTime.now()
            val addGroceryItemDialog = AlertDialog.Builder(this).apply {
                setTitle("Enter The Grocery Item")
                setMessage("Enter The Grocery Item,its Number and SuperMarket")
                setView(view)
                setPositiveButton("Add"){dialog, which ->
                    val itemName = view.edtv_itemName.text.toString()
                    val superMarketName = view.edtv_superMarketName.text.toString()
                    val numberOfItem = view.edtv_numOfItem.text.toString().toFloat()
                    val day = currentDateTime.dayOfMonth
                    val month = currentDateTime.monthValue
                    val year = currentDateTime.year
                    val hour = currentDateTime.hour
                    val min = currentDateTime.hour
                    val sec = currentDateTime.second
                    val date = DateFormat(day, month, year, hour, min, sec)
                    val groceryItem = GroceryItem(0,itemName,superMarketName, numberOfItem!!,date)
                    Log.i("MainActivity",itemName)
                    Log.i("MainActivity",superMarketName)
                    if(TextUtils.isEmpty(itemName) && TextUtils.isEmpty(superMarketName))
                    {
                        Toast.makeText(this@MainActivity,"Form Canot Be Empty",Toast.LENGTH_LONG).show()
                    }
                    else{
                        groceryviewmodel.addItem(groceryItem)
                        Toast.makeText(this@MainActivity,"Item Added Successfully",Toast.LENGTH_LONG).show()
                    }
                }
                setNegativeButton("Cancel"){dialog, which ->

                }
            }

            addGroceryItemDialog.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainactivitymenu,menu)

        val search = menu?.findItem(R.id.mn_search)
        val searchview = search?.actionView as androidx.appcompat.widget.SearchView
        searchview?.isSubmitButtonEnabled = true
        searchview?.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText!=null){
            searchDatabseUser(newText)
        }
        return true
    }

    fun searchDatabseUser(queryfrom: String) {
        var serachQuery = "%$queryfrom%"
        groceryviewmodel.getResultBySearchItem(serachQuery).observe(this@MainActivity, Observer {
            groceryItemAdapter.setData(it)
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mn_delete->{
                val deleteAllUserAlert = AlertDialog.Builder(this).apply {
                    setTitle("Delete All Item")
                    setMessage("Are You Sure You Want To Delete All Item ?")
                    setPositiveButton("Yes"){dialog, which ->
                        groceryviewmodel.deleteAllItem()
                    }
                    setNegativeButton("No"){dialog, which ->

                    }

                }
                deleteAllUserAlert.show()
            }
            R.id.mn_exit->{
                val exitAppAlert = AlertDialog.Builder(this).apply {
                    setTitle("Exit The Application")
                    setMessage("Are You Sure You Exit The Application ?")
                    setPositiveButton("Yes"){dialog, which ->
                        finishAffinity()
                    }
                    setNegativeButton("No"){dialog, which ->

                    }

                }
                exitAppAlert.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}