package com.example.okaz.Logic

data class User(val Credit:String?,val Image:String?,val Name:String?,val Phone:String? ,val uid:String?) {
    constructor():this(null,null,null,null,null)
}