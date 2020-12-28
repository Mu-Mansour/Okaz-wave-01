package com.example.okaz.Ui.Cart

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.okaz.Logic.CartProduct
import com.example.okaz.Logic.Utility
import com.example.okaz.Repo.Repo

class CartViewModel @ViewModelInject constructor(private val theAppRepoForAll: Repo): ViewModel() {
    val theProductsForCart:MutableLiveData<List<CartProduct>> = MutableLiveData()


    fun makeTheListFromUtitity()
    {

            val theListToBeAdded:MutableList<CartProduct> = mutableListOf()
            for (i in Utility.theOrder)
            {
                theListToBeAdded.add(CartProduct(i.key.image!!,i.key.Name!!,i.key.Category!!,i.key.Price!!,i.value))
                Utility.cartPrice.value = Utility.cartPrice.value?.plus(i.value.toDouble())
            }

            theProductsForCart.value=theListToBeAdded
            Utility.theOrder.clear()
    }

}