package com.example.okaz.Ui.Cart

import android.app.ProgressDialog
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okaz.Logic.CartProduct
import com.example.okaz.Logic.Utility
import com.example.okaz.Repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel @ViewModelInject constructor(private val theAppRepoForAll: Repo): ViewModel() {
    val theProductsForCart:MutableLiveData<List<CartProduct>> = MutableLiveData()
    var theAdress:String?=null
    var PhoneToContact:String?=null
    var theReciver:String?=null

    fun makeTheListFromUtitity()
    {

            val theListToBeAdded:MutableList<CartProduct> = mutableListOf()
            for (i in Utility.theOrder)
            {
                theListToBeAdded.add(CartProduct(i.key.image!!,i.key.Name!!,i.key.Category!!,i.key.Price!!,i.value,i.key.id!!))
                Utility.cartPrice.value = Utility.cartPrice.value?.plus(i.value.toDouble())
            }

            theProductsForCart.value=theListToBeAdded
    }
    fun updateToDataBaseOrder(dialouge:ProgressDialog)
    {
        val theOrderId=FirebaseDatabase.getInstance().reference.push().key.toString()
        Utility.theFinalOrder?.let {
            it["id"]=theOrderId
            it["theAddress"]=theAdress!!
            it["PhoneToContact"]=PhoneToContact!!
            it["theReceiver"]=theReciver!!
            it["price"]=Utility.cartPrice.value.toString()
            it["WhoOrdered"]=FirebaseAuth.getInstance().currentUser!!.uid
            FirebaseDatabase.getInstance().reference.child("PendingOrders")
                   .child(theOrderId).updateChildren(it as Map<String, Any>).addOnSuccessListener {_->
                        it.clear()
                        Utility.theFinalOrder=null
                      theAdress=null
                       PhoneToContact=null
                       theReciver=null
                        Utility.theOrder.clear()
                        Utility.cartPrice.value=0.0

                viewModelScope.launch(Dispatchers.Main) {
                    dialouge.setMessage("Thank You For Trusting Us ")
                    delay(3000)
                    dialouge.dismiss()
                }
            }
        }

    }

}