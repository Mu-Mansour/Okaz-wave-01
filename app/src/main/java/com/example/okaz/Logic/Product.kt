package com.example.okaz.Logic

data class Product(val Name:String?,val Description:String?,val Price:String?,val image:String?,val id:String?,val Category:String?) {
    constructor():this(null,null,null,null,null,null)
}