package com.example.okaz.Ui.Account

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okaz.Repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AcoountSettingsViewModel @ViewModelInject constructor(private val theAppRepoForAll: Repo): ViewModel() {
    var theUserImage:String?=null
    fun getTheUserImage()=FirebaseDatabase.getInstance().reference.child("Users")
        .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Image")

    fun storeAndUpdateImage(theFire:Uri){
        val imageFileRefrence = FirebaseStorage.getInstance().reference.child("UsersProflePics")
            .child("${FirebaseAuth.getInstance().currentUser!!.uid}.png")
        imageFileRefrence.putFile(theFire).addOnCompleteListener {
            if (it.isSuccessful) {
                imageFileRefrence.downloadUrl.addOnSuccessListener { theLinkCreated ->

                    FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Image").setValue(theLinkCreated.toString())
                }
            }


        }
    }
}