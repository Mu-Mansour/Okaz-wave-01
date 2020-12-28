package com.example.okaz.Logic

import androidx.lifecycle.MutableLiveData

object Utility {

    val cartPrice:MutableLiveData<Double> = MutableLiveData()
    val theOrder:HashMap<Product,String> = HashMap()
}