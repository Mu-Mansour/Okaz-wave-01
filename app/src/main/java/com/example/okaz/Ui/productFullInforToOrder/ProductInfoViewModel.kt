package com.example.okaz.Ui.productFullInforToOrder

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okaz.Logic.Product
import com.example.okaz.Repo.Repo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductInfoViewModel @ViewModelInject constructor(private val theAppRepoForAll: Repo): ViewModel() {
    val theProductLiveObject:MutableLiveData<Product> = MutableLiveData()
    var theNewName:String?=null
    var theNewPrice:String?=null
    var theNewDescreption:String?=null
    val TaskCompletedForAdmin:MutableLiveData<Boolean> = MutableLiveData()


    var theQuantity= 0

    fun updateTheLiveProduct(id: String,cat: String)=viewModelScope.launch(Dispatchers.IO) {

        FirebaseDatabase.getInstance().reference.child("Products").child(cat).child(id).addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    viewModelScope.launch(Dispatchers.Main) {
                        theProductLiveObject.value=snapshot.getValue(Product::class.java)!!
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    fun updateTheLiveHotProduct(id: String)=viewModelScope.launch(Dispatchers.IO) {

        FirebaseDatabase.getInstance().reference.child("HotProducts").child(id).addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    viewModelScope.launch(Dispatchers.Main) {
                        theProductLiveObject.value=snapshot.getValue(Product::class.java)!!

                    }
                    FirebaseDatabase.getInstance().reference.child("HotProducts").child(id).removeEventListener(this)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun updateThatProductForAdmin()
    {
        theProductLiveObject.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                FirebaseDatabase.getInstance().reference.child("Products")
                        .child(it.Category!!).child(it.id!!).child("Name").setValue(theNewName!!).addOnCompleteListener {_->
                            FirebaseDatabase.getInstance().reference.child("Products")
                                    .child(it.Category).child(it.id).child("Description").setValue(theNewDescreption!!).addOnSuccessListener {_->
                                        FirebaseDatabase.getInstance().reference.child("Products")
                                                .child(it.Category).child(it.id).child("Price").setValue(theNewPrice!!).addOnSuccessListener {_->
                                                    FirebaseDatabase.getInstance().reference.child("SearchTree")
                                                            .child(it.id).child("name").setValue(theNewName!!).addOnSuccessListener {
                                                                TaskCompletedForAdmin.value=true
                                                            }

                                                }
                                    }
                        }
            }
        }

    }
    fun deleteTheProduct()
    {
        theProductLiveObject.value?.let {
            FirebaseDatabase.getInstance().reference.child("Products")
                    .child(it.Category!!).child(it.id!!).removeValue().addOnSuccessListener {_->
                        FirebaseDatabase.getInstance().reference.child("SearchTree")
                                .child(it.id).removeValue().addOnSuccessListener {
                                    theProductLiveObject.value=null
                                    TaskCompletedForAdmin.value=true
                                }
                    }

        }

    }
}