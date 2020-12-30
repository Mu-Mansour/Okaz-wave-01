package com.example.okaz.Logic

data class OrderItem(val category:String?,val quantity:String? ) {
    constructor():this(null,null)
}