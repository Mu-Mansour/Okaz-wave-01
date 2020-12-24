package com.example.okaz.Logic

data class Product(val name:String?,val quantity:Int?,val price:Double?,val image:Int?,val id:String?) {
    constructor():this(null,null,null,null,null)
}