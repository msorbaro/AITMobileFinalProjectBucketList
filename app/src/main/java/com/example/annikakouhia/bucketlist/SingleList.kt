package com.example.annikakouhia.bucketlist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.annikakouhia.bucketlist.adapter.ItemAdapter
import com.example.annikakouhia.bucketlist.data.BucketListItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_single_list.*



class SingleList : AppCompatActivity(), AuthorDialog.ItemHandler, AddItemInBucketDialog.ItemHandler {

    private lateinit var currList: String
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_list)

        addAuth.setOnClickListener { view ->
            addAuthor()
        }

        addItemBucket.setOnClickListener{view ->
            addItemToBucker()
        }

        if (intent.hasExtra(getString(R.string.currlistcaps))) {
            currList = (intent.getStringExtra(getString(R.string.currlistcaps)))
        }


        fillAuthors()

        itemAdapter = ItemAdapter(this, FirebaseAuth.getInstance().currentUser!!.uid, currList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerInList.layoutManager = layoutManager
        recyclerInList.adapter = itemAdapter

        initItems()

    }

    fun initItems() {
        val db = FirebaseFirestore.getInstance()

        val allLists = db.collection(getString(R.string.blistlast))
        allLists.addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                if (p1 != null) {
                    return
                }

                for (docChange in querySnapshot!!.getDocumentChanges()) {


                    if(docChange.document.get(getString(R.string.titlesinglelist)) == currList) {


                        if(docChange.document.get(getString(R.string.itemsinglelist)) != null){

                            val allItems = docChange.document.get(getString(R.string.itemsinglelist)) as List<BucketListItem>
                            itemAdapter.clearAll()
                            for(i in 0..allItems.size - 1){
                                var theMap = allItems.get(i) as HashMap<String, Any>
                                var isTrue = theMap.get(getString(R.string.checkedsinglelist)).toString().toBoolean()
                                var theItem = BucketListItem(theMap.get(getString(R.string.itemnamesingle)).toString(), isTrue)
                                itemAdapter.addItem(theItem, docChange.document.id)
                            }
                        }
                    }
                }
            }
        })
    }

    fun fillAuthors() {



        val db = FirebaseFirestore.getInstance()

        val allLists = db.collection(getString(R.string.blistlast))
        allLists.addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                if (p1 != null) {
                    Toast.makeText(this@SingleList, "Error: ${p1.message}",
                            Toast.LENGTH_LONG).show()
                    return
                }

                for (docChange in querySnapshot!!.getDocumentChanges()) {
                    if(docChange.document.get(getString(R.string.titlesinglelist)) == currList) {
                        val authors = docChange.document.get(getString(R.string.authlast)) as List<String>
                        allAuthors.setText("Members: \n")
                        for (i in 0..authors.size - 1) {
                            allAuthors.append("\t" + authors.get(i) + "\n")

                        }
                    }
                }
            }
        })
    }

    fun addAuthor(){
        AuthorDialog().show(supportFragmentManager, getString(R.string.tagcreate))
    }

    fun addItemToBucker(){
        AddItemInBucketDialog().show(supportFragmentManager, getString(R.string.tagcreate))
    }

    override fun authorAdded(item: String) {
        if(checkValidity(item)){

            val db = FirebaseFirestore.getInstance()

            val allLists = db.collection(getString(R.string.blistlast))

            allLists.addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                    if (p1 != null) {
                        Toast.makeText(this@SingleList, "Error: ${p1.message}",
                                Toast.LENGTH_LONG).show()
                        return
                    }

                    for (docChange in querySnapshot!!.getDocumentChanges()) {
                        if(docChange.document.get(getString(R.string.titlesinglelist)) == currList){
                              docChange.document.reference.update(getString(R.string.authlastone), FieldValue.arrayUnion(item))
                        }
                    }
                }
            })
        }
        else{
            Toast.makeText(this@SingleList, getString(R.string.erroradding), Toast.LENGTH_SHORT).show()
        }
    }

    override fun itemBucketAdded(item: String) {

            val db = FirebaseFirestore.getInstance()

            val allLists = db.collection(getString(R.string.blistlast))

            allLists.addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                    if (p1 != null) {
                        return
                    }

                    for (docChange in querySnapshot!!.getDocumentChanges()) {
                        if(docChange.document.get(getString(R.string.titlelastsingle)) == currList){


                            val map = mapOf<String, Any>(
                                    getString(R.string.itemnamelast) to item,
                                    getString(R.string.checkedsinglelist) to false
                            )
                            docChange.document.reference.update(getString(R.string.itemfinalitem), FieldValue.arrayUnion(map))


                        }
                    }
                }
            })

    }

    fun checkValidity(str: String): Boolean {
        if(FirebaseAuth.getInstance().currentUser!!.email.toString()==str){
            Toast.makeText(this@SingleList, getString(R.string.noaddseld), Toast.LENGTH_SHORT).show()
            return false
        }
        if(FirebaseAuth.getInstance().createUserWithEmailAndPassword(str, getString(R.string.lotofeights)).isSuccessful){
            Toast.makeText(this@SingleList, getString(R.string.usernoexis), Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this@SingleList, getString(R.string.founduser), Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }
}
