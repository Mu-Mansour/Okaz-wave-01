package com.example.okaz.Ui.logInCst

import android.app.Activity
import android.widget.EditText
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okaz.Repo.Repo
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LogInCstViewModel @ViewModelInject constructor(): ViewModel(){
    var theName:String?=null
    var thePhoneNumber:String?=null
    var theUthKey:String?=null
    var theCodeSent:String?=null
    val userExist:MutableLiveData<Boolean> = MutableLiveData()



    fun takeDataAndSignUp(activity: Activity): Unit? {


       return thePhoneNumber?.let {
            val theCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(activity, "${p0.message}", Toast.LENGTH_SHORT).show()

                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    theCodeSent = p0
                }

            }

            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                    .setPhoneNumber("+2${it.trim()}")       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit // Activity (for callback binding)
                    .setActivity(activity)
                    .setCallbacks(theCallBack)          // OnVerificationStateChangedCallbacks
                    .build()

          return@let  PhoneAuthProvider.verifyPhoneNumber(options)

        }
    }
    fun verifyTheNewCustomer(): Task<AuthResult>? {

      return  theUthKey?.let {
            FirebaseAuth.getInstance().signInWithCredential(PhoneAuthProvider.getCredential(theCodeSent!!, theUthKey!!))
        }



    }

    fun checkToCreate()=FirebaseDatabase.getInstance().reference.child("Users")
            .child( FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        viewModelScope.launch(Dispatchers.Main) {
                            userExist.value=true
                        }

                    }
                    else
                    {
                        viewModelScope.launch(Dispatchers.Main) {
                            userExist.value=false
                            withContext(Dispatchers.IO){
                                createTheCustomerDetails()
                            }
                        }



                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    fun createTheCustomerDetails(){
            val theMap = HashMap<String, Any>()
            theMap["uid"] =  FirebaseAuth.getInstance().currentUser!!.uid
            theMap["Image"] = "https://firebasestorage.googleapis.com/v0/b/okaz-8c8c3.appspot.com/o/defaultProfileImage%2FdefaultUsers.png?alt=media&token=6ffe3516-4e23-4110-a7df-8bc923b2a08f"
            theMap["Name"] = theName!!
            theMap["Phone"] = thePhoneNumber!!
            theMap["Credit"] = "0.0"
        FirebaseDatabase.getInstance().reference.child("Users").child( FirebaseAuth.getInstance().currentUser!!.uid).updateChildren(theMap)

        }



}