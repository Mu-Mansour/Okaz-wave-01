package com.example.okaz.Ui.Splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.okaz.Repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SplashViewModel @ViewModelInject constructor(): ViewModel() {
    fun checkMyRefrence()=FirebaseDatabase.getInstance().reference.child("Admins").child(FirebaseAuth.getInstance().currentUser!!.uid)
}