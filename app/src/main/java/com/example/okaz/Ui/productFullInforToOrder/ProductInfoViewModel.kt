package com.example.okaz.Ui.productFullInforToOrder

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.okaz.Logic.Product
import com.example.okaz.Repo.Repo
import com.google.firebase.database.FirebaseDatabase

class ProductInfoViewModel @ViewModelInject constructor(private val theAppRepoForAll: Repo): ViewModel() {
    var theQuantity= 0
    var theProduct:Product?=null
    fun getTheProductFromCategories(id:String, cat:String)=FirebaseDatabase.getInstance().reference.child("Products").child(cat).child(id)
    fun getTheProductFromHot(id:String)=FirebaseDatabase.getInstance().reference.child("HotProducts").child(id)
}