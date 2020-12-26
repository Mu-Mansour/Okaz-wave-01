package com.example.okaz.Ui.HomeFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.okaz.Logic.Product
import com.example.okaz.Repo.Repo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel@ViewModelInject constructor(private val theAppRepoForAll: Repo): ViewModel() {
     val theHotProductsForCsts: MutableLiveData<List<Product>> = MutableLiveData()

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
                    theHotProductsForCsts.value=productList
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}