package com.example.okaz.Ui.AdminLogin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okaz.Repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminLoginViewModel@ViewModelInject constructor(): ViewModel() {

    var theEmail:String?=null
    var thePasswordl:String?=null
    val checking:MutableLiveData<Boolean> = MutableLiveData()
    var theError:String?=null
    var theResult=false
   fun logInForAdmin()=FirebaseAuth.getInstance().signInWithEmailAndPassword(theEmail!!,thePasswordl!!).addOnSuccessListener {
           FirebaseDatabase.getInstance().reference.child("Admins")
                   .child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object:ValueEventListener{
                       override fun onDataChange(snapshot: DataSnapshot) {
                           if (snapshot.exists())
                           {
                               theResult=true
                               viewModelScope.launch(Dispatchers.Main) {
                                   checking.value=theResult
                               }
                           }
                           else
                           {
                               viewModelScope.launch(Dispatchers.Main) {
                                   theError="Un Authorized Log In"
                                   checking.value=theResult
                               }
                           }
                       }

                       override fun onCancelled(error: DatabaseError) {
                       }
                   })
       }.addOnFailureListener {
       theError=it.message
       checking.value=theResult

   }

}