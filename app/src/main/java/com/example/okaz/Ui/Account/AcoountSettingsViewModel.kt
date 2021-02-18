package com.example.okaz.Ui.Account

import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AcoountSettingsViewModel @ViewModelInject constructor(): ViewModel() {

    val theUserImage:MutableLiveData<String> = MutableLiveData()
    fun getTheUserImage()=FirebaseDatabase.getInstance().reference.child("Users")
        .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Image").addValueEventListener(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        viewModelScope.launch(Dispatchers.Main) {
                            theUserImage.value=snapshot.value.toString()
                        }

                        FirebaseDatabase.getInstance().reference.child("Users")
                                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Image").removeEventListener(this)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

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