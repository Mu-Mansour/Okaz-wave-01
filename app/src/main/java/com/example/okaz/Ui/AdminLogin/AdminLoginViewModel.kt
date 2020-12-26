package com.example.okaz.Ui.AdminLogin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.okaz.Repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminLoginViewModel@ViewModelInject constructor(private val theAppRepoForAll: Repo): ViewModel() {

    var theEmail:String?=null
    var thePasswordl:String?=null

   fun logInForAdmin()=theAppRepoForAll.logInForAdmin(theEmail!!,thePasswordl!!)
    fun refrenceToAdmins()=FirebaseDatabase.getInstance().reference.child("Admins").child(FirebaseAuth.getInstance().currentUser!!.uid)
}