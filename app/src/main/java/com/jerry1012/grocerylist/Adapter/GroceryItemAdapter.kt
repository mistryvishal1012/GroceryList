package com.jerry1012.grocerylist.Adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.jerry1012.grocerylist.R
import com.jerry1012.grocerylist.ViewModel.GroceryItemViewModel
import com.jerry1012.grocerylist.model.DateFormat
import com.jerry1012.grocerylist.model.GroceryItem
import kotlinx.android.synthetic.main.addgroceryitemdialog_layout.view.*
import kotlinx.android.synthetic.main.recyclerview_groceryitem_layout.view.*
import java.time.LocalDateTime

class GroceryItemAdapter(groceryviewmodel : GroceryItemViewModel,private val activity: AppCompatActivity) : RecyclerView.Adapter<GroceryItemAdapter.GroceryItemViewHolder>(), ActionMode.Callback{
    private var tempgroceryviewmodel : GroceryItemViewModel = groceryviewmodel
    private lateinit var tempparent:ViewGroup
    private lateinit var tempcontext : Context
    private var multiSelect = false
    private val selectedItems = arrayListOf<GroceryItem>()
    private val selectedItemView = arrayListOf<View>()
    private lateinit var tempholder : GroceryItemViewHolder

    var groceryItemList = emptyList<GroceryItem>()
    inner class GroceryItemViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryItemViewHolder {
        tempparent = parent
        tempcontext = parent.context
        return GroceryItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_groceryitem_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return groceryItemList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: GroceryItemViewHolder, position: Int) {

        tempholder = holder
        val numberOfPosition = position + 1
        val currentGroceryItem = groceryItemList[position]
        val currentDateTime = LocalDateTime.now()
        var curritemName = currentGroceryItem.ItemName
        var curritemId = currentGroceryItem.id
        var currsuperMarketName = currentGroceryItem.SuperMarket
        var currnumberOfItem = currentGroceryItem.NumberOfItem
        var date = currentGroceryItem.date
        holder.itemView.tv_positionNumber.text = numberOfPosition.toString()
        holder.itemView.tv_item.text = currentGroceryItem.ItemName
        holder.itemView.tv_supermarket.text = currentGroceryItem.SuperMarket
        holder.itemView.tv_numberofitem.text = currentGroceryItem.NumberOfItem.toString()
        if(currnumberOfItem < 0)
        {
            holder.itemView.btn_minusitem.isEnabled = false
        }

        holder.itemView.btn_additem.setOnClickListener {
            currnumberOfItem = currnumberOfItem + 1
            var tempGroceryItem = GroceryItem(curritemId,curritemName,currsuperMarketName,currnumberOfItem,date)
            tempgroceryviewmodel.updateItem(tempGroceryItem)
            if(currnumberOfItem > 0){
                holder.itemView.btn_minusitem.isEnabled = true
            }
        }
        holder.itemView.btn_minusitem.setOnClickListener {
            currnumberOfItem = currnumberOfItem - 1
            var tempGroceryItem = GroceryItem(curritemId,curritemName,currsuperMarketName,currnumberOfItem,date)
            tempgroceryviewmodel.updateItem(tempGroceryItem)
            if(currnumberOfItem < 0){
                holder.itemView.btn_minusitem.isEnabled = false
            }
        }
        holder.itemView.btn_shareitem.setOnClickListener {
            val string = "The $curritemName  Of Count  $currnumberOfItem Should Be Purchased From  $currsuperMarketName "
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, string)
            intent.type = "text/plain"
            tempcontext.startActivity(Intent.createChooser(intent, "Share The Grocery Item"))
        }
        holder.itemView.btn_deletitem.setOnClickListener {
            var tempGrocerItem = GroceryItem(curritemId,curritemName,currsuperMarketName,currnumberOfItem,date)
            tempgroceryviewmodel.deleteItem(tempGrocerItem)
        }
        holder.itemView.tv_item.setOnClickListener {
            var groceryItem = GroceryItem(curritemId,curritemName,currsuperMarketName,currnumberOfItem,date)
            if (multiSelect) {
                if(selectedItems.contains(groceryItem))
                {
                    selectedItems.remove(groceryItem)
                    selectedItemView.remove(holder.itemView)
                    holder.itemView.alpha = 1f
                }
                else
                {
                    selectedItems.add(groceryItem)
                    selectedItemView.add(holder.itemView)
                    holder.itemView.alpha = 0.5f
                }
            } else {
                val updateDialog = AlertDialog.Builder(tempcontext!!).apply {
                    val view = LayoutInflater.from(tempcontext).inflate(R.layout.addgroceryitemdialog_layout, tempparent, false)
                    setTitle("Update The Item")
                    setMessage("Update The Field You Want To")
                    setView(view)
                    view?.edtv_itemName?.setText(curritemName)
                    view?.edtv_superMarketName?.setText(currsuperMarketName)
                    view?.edtv_numOfItem?.setText(currnumberOfItem.toString())
                    setPositiveButton("Update") { dialog, which ->
                        val itemName = view.edtv_itemName.text.toString()
                        val superMarketName = view.edtv_superMarketName.text.toString()
                        val numberOfItem = view.edtv_numOfItem.text.toString().toInt()
                        val day = currentDateTime.dayOfMonth
                        val month = currentDateTime.monthValue
                        val year = currentDateTime.year
                        val hour = currentDateTime.hour
                        val min = currentDateTime.hour
                        val sec = currentDateTime.second
                        val date = DateFormat(day, month, year, hour, min, sec)
                        Log.i("MainActivity", itemName)
                        Log.i("MainActivity", superMarketName)
                        if (TextUtils.isEmpty(itemName) && TextUtils.isEmpty(superMarketName) && numberOfItem == 0) {
                            view?.edtv_itemName?.setBackgroundResource(R.drawable.inputmissing_layout)
                            view?.edtv_superMarketName?.setBackgroundResource(R.drawable.inputmissing_layout)
                            view?.edtv_numOfItem?.setBackgroundResource(R.drawable.inputmissing_layout)
                            Toast.makeText(tempcontext, "Form Canot Be Empty", Toast.LENGTH_LONG).show()
                        } else {
                            val groceryItem = GroceryItem(curritemId, itemName, superMarketName, numberOfItem!!.toFloat(), date)
                            tempgroceryviewmodel.updateItem(groceryItem)
                            Toast.makeText(tempcontext, "Item Added Successfully", Toast.LENGTH_LONG).show()
                        }
                    }
                    setNegativeButton("Cancel") { dialog, which ->

                    }
                }
                updateDialog.show()
            }
        }

        holder.itemView.setOnLongClickListener {
            if (!multiSelect) {
                var tempgroceryItem = GroceryItem(curritemId,curritemName,currsuperMarketName,currnumberOfItem,date)
                multiSelect = true
                activity.startSupportActionMode(this)
                holder.itemView.alpha = 0.5f
                selectedItems.add(tempgroceryItem)
                selectedItemView.add(holder.itemView)
                true
            }
            true
        }


    }


    fun setData(tempgroceryItemList : List<GroceryItem>){
        groceryItemList = tempgroceryItemList
        notifyDataSetChanged()
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.mn_ada_delete-> {
                if (selectedItems.size == 0) {
                    Toast.makeText(tempcontext, "Please Select Items First", Toast.LENGTH_LONG).show()
                } else {
                    for (i in selectedItems) {
                        var tempIndex = selectedItems.indexOf(i)
                        tempgroceryviewmodel.deleteItem(i)
                        selectedItemView[tempIndex].alpha = 1f
                    }
                    Toast.makeText(tempcontext, "Items Deleted Successfully", Toast.LENGTH_LONG).show()
                    mode?.finish()
                }
            }
            R.id.mn_ada_share->{
                if (selectedItems.size == 0) {
                    Toast.makeText(tempcontext, "Please Select Items First", Toast.LENGTH_LONG)
                        .show()
                } else {
                    var shareString:String= ""
                    for (i in selectedItems) {
                        var tempIndex = selectedItems.indexOf(i)
                        shareString = shareString + "The ${i.ItemName}  Of Count  ${i.SuperMarket} Should Be Purchased From  ${i.SuperMarket} \n"
                        selectedItemView[tempIndex].alpha = 1f
                    }
                    Log.i("Sting",shareString)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_TEXT, shareString)
                    intent.type = "text/plain"
                    tempcontext.startActivity(Intent.createChooser(intent, "Share The Grocery Item"))
                    mode?.finish()
                }

            }
            else -> {
                for (i in selectedItemView){
                    i.alpha = 1f
                }
                selectedItemView.clear()
                selectedItems.clear()
            }
        }

        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val inflater: MenuInflater = mode?.menuInflater!!
        inflater.inflate(R.menu.selecteditems_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiSelect = false
        selectedItems.clear()
        selectedItemView.clear()
        notifyDataSetChanged()
    }

}