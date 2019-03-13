package com.example.annikakouhia.bucketlist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.annikakouhia.bucketlist.R
import android.widget.*
import com.example.annikakouhia.bucketlist.data.BucketListItem
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.row_item.view.*


class ItemAdapter(var context: Context, var uid:String, var currList:String) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var itemList = mutableListOf<BucketListItem>()
    private var itemKeys = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.row_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList.get(holder.adapterPosition)
        holder.itemTitle.text = item.itemName
        holder.itemChecked.isChecked = item.checked
        holder.itemChecked.setOnClickListener {
            item.checked = holder.itemChecked.isChecked()
            addToDatabase(item.itemName, item.checked)
        }
    }

    fun clearAll(){
        itemList = mutableListOf<BucketListItem>()
    }

    fun addToDatabase(itemName: String, checked: Boolean){
        val allLists = FirebaseFirestore.getInstance().collection(context.getString(R.string.bucketlist))
        allLists.addSnapshotListener(object: EventListener<QuerySnapshot> {

            override fun onEvent(querySnapshot: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                if (p1 != null) { return }

                for (docChange in querySnapshot!!.getDocumentChanges()) {
                    if(docChange.document.get(context.getString(R.string.title)) == currList){
                        val map = mapOf<String, Any>(context.getString(R.string.itemName) to itemName, context.getString(R.string.checked) to checked)
                        val oldMap = mapOf<String, Any>(context.getString(R.string.itemName) to itemName, context.getString(R.string.checked) to !checked)
                        docChange.document.reference.update(context.getString(R.string.items), FieldValue.arrayRemove(oldMap))
                        docChange.document.reference.update(context.getString(R.string.items), FieldValue.arrayUnion(map))

                    }
                }
            }
        })
    }

    fun addItem(item: BucketListItem, key: String) {
        itemList.add(item)
        itemKeys.add(key)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.titleItem
        val itemChecked: CheckBox = itemView.checkBoxItem

    }

}