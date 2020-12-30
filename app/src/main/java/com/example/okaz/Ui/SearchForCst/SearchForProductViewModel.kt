package com.example.okaz.Ui.SearchForCst

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.okaz.Logic.ItemForSearchTotall
import com.example.okaz.Repo.Repo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchForProductViewModel @ViewModelInject constructor(private val theAppRepoForAll: Repo): ViewModel() {

    val theListToBeAdded:MutableLiveData<MutableList<ItemForSearchTotall>> = MutableLiveData()
    fun theSearchJob(word:String)
    {
     val theRef=FirebaseDatabase.getInstance().reference.child("SearchTree")
         .orderByChild("Name").startAt(word).endAt((word+"\uf8ff")).addValueEventListener(object: ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 if (snapshot.exists())
                 {
                     theListToBeAdded.value= mutableListOf()
                     for (i in snapshot.children)
                     {
                         val theItem = i.getValue(ItemForSearchTotall::class.java)
                         theItem!!.id=i.key!!
                         theListToBeAdded.value!!.add(theItem)

                     }
                 }
             }

             override fun onCancelled(error: DatabaseError) {
             }
         })


    }
}