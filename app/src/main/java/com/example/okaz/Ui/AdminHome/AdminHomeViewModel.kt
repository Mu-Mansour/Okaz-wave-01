package com.example.okaz.Ui.AdminHome

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okaz.Logic.ItemForSearchTotall
import com.example.okaz.Logic.Product
import com.example.okaz.Repo.Repo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class AdminHomeViewModel @ViewModelInject constructor(): ViewModel() {
    var theCategory:String?=null
    var theUriForImage: Uri?=null
    var theProductNmae:String?=null
    var theProductPrice:String?=null
    var theProductDescription:String?=null
    val jobIsDone:MutableLiveData<Boolean> = MutableLiveData()
    val theHotProducts:MutableLiveData<List<Product>> = MutableLiveData()
    val thePendingOrders:MutableLiveData<List<String>> = MutableLiveData()
    fun mkaeTheProductMapAndUploadIt (): Deferred<StorageTask<UploadTask.TaskSnapshot>> {

        val theProductId=FirebaseDatabase.getInstance().reference.push().key!!

    return   viewModelScope.async {
           val imageFileRefrence = FirebaseStorage.getInstance().reference.child("ProductsImages").child("${theProductId}.png")
           imageFileRefrence.putFile(theUriForImage!!).addOnCompleteListener {
               if (it.isSuccessful) {
                   imageFileRefrence.downloadUrl.addOnSuccessListener { theLinkCreated ->
                      val  theProductMap=HashMap<String,Any>()
                       theProductMap["Name"]=theProductNmae!!
                       theProductMap["Price"]=theProductPrice!!.toString()
                       theProductMap["Description"]=theProductDescription!!
                       theProductMap["Category"]=theCategory!!
                       theProductMap["image"] = theLinkCreated.toString()
                       theProductMap["id"] = theProductId
                       FirebaseDatabase.getInstance().reference.child("Products").child(theCategory!!).child(theProductId).updateChildren(
                           theProductMap
                       ).addOnSuccessListener {
                           FirebaseDatabase.getInstance().reference.child("SearchTree").child(theProductId).setValue(ItemForSearchTotall(theProductId,theProductNmae!!,theCategory!!)).addOnSuccessListener {
                                   viewModelScope.launch(Dispatchers.Main ){
                                       jobIsDone.value=true
                                       theUriForImage=null
                                       theProductNmae=null
                                       theProductPrice=null
                                       theProductDescription=null
                                       theProductMap.clear()

                                   }
                               }




                       }
                   }
               }


           }
       }
    }
    fun mkaeTheHotProductMapAndUploadIt (): Deferred<StorageTask<UploadTask.TaskSnapshot>> {

        val theProductId=FirebaseDatabase.getInstance().reference.push().key!!

        return   viewModelScope.async {
            val imageFileRefrence = FirebaseStorage.getInstance().reference.child("ProductsImages").child("${theProductId}.png")
            imageFileRefrence.putFile(theUriForImage!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    imageFileRefrence.downloadUrl.addOnSuccessListener { theLinkCreated ->
                        val theProductMap = HashMap<String, Any>()
                        theProductMap["Name"] = theProductNmae!!.toLowerCase(Locale.ROOT)
                        theProductMap["Price"] = theProductPrice!!.toString().toLowerCase(Locale.ROOT)
                        theProductMap["Description"] = theProductDescription!!.toLowerCase(Locale.ROOT)
                        theProductMap["Category"] = theCategory!!
                        theProductMap["image"] = theLinkCreated.toString()
                        theProductMap["id"] = theProductId
                        FirebaseDatabase.getInstance().reference.child("HotProducts").child(theProductId).updateChildren(theProductMap!!).addOnSuccessListener {
                            viewModelScope.launch(Dispatchers.IO) {
                                FirebaseDatabase.getInstance().reference.child("Products").child(theCategory!!).child(theProductId).updateChildren(theProductMap!!).addOnSuccessListener {
                                    FirebaseDatabase.getInstance().reference.child("SearchTree").child(theProductId).setValue(ItemForSearchTotall(theProductId, theProductNmae!!.toLowerCase(Locale.ROOT), theCategory!!)).addOnSuccessListener {
                                        viewModelScope.launch(Dispatchers.Main) {
                                            viewModelScope.launch(Dispatchers.Main ){
                                                jobIsDone.value=true
                                                theUriForImage=null
                                                theProductNmae=null
                                                theProductPrice=null
                                                theProductDescription=null
                                                theProductMap.clear()

                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }


            }
        }
    }
    fun getTheHotProducts()
    {
        var productList = mutableListOf<Product>()
        FirebaseDatabase.getInstance().reference.child("HotProducts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                if (snapshot.exists()) {
                    for (NOTS in snapshot.children) {
                        val theProduct = NOTS.getValue(Product::class.java)
                        productList.add(theProduct!!)


                    }
                    theHotProducts.value=productList
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun getThePendingOrders()
    {
        var OrdersList = mutableListOf<String>()
        FirebaseDatabase.getInstance().reference.child("PendingOrders").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                OrdersList.clear()
                if (snapshot.exists()) {
                    for (NOTS in snapshot.children) {
                        val theOrder = NOTS.key.toString()
                        OrdersList.add(theOrder)


                    }
                    thePendingOrders.value=OrdersList
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}