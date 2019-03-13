package com.example.annikakouhia.bucketlist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.annikakouhia.bucketlist.data.BucketList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_bucket_list.*

class CreateBucketList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_bucket_list)

        btnSend.setOnClickListener{
            uploadBucketList()
        }
    }

    private fun uploadBucketList(){
        var uidList = mutableListOf(FirebaseAuth.getInstance().currentUser?.uid.toString())
        var emailList = mutableListOf(FirebaseAuth.getInstance().currentUser?.email.toString())
        val bList = BucketList(uidList, emailList, etTitle.text.toString())

        val bListCollections = FirebaseFirestore.getInstance().collection(getString(R.string.bucketlists))
        bListCollections.add(bList)
                .addOnSuccessListener { Toast.makeText(this@CreateBucketList, getString(R.string.blistsaved), Toast.LENGTH_LONG).show() }
                .addOnFailureListener{ Toast.makeText(this@CreateBucketList, "Error ${it.message}", Toast.LENGTH_LONG).show() }
    }


}
