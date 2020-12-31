package com.example.okaz.Ui.SearchForCst

import android.text.Editable
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
    val theListedSearch:MutableList<ItemForSearchTotall> = mutableListOf()

    fun theSearchJob(word: Editable?)
    {
        if (word.toString()!= "")
        {

        theListedSearch.clear()
        val theRef=FirebaseDatabase.getInstance().reference.child("SearchTree")
                .orderByChild("name").startAt(word.toString()).endAt((word.toString()+"\uf8ff")).addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists())
                        {
                            for (i in snapshot.children)
                            {
                                val theItem = i.getValue(ItemForSearchTotall::class.java)
                                theItem?.let {
                                    if (!theListedSearch.contains(theItem))
                                   theListedSearch.add(theItem)
                                  theListToBeAdded.value=theListedSearch

                                }


                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

        }
    }

}