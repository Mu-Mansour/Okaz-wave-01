package com.example.okaz.Ui.PendingOrderForAdmin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.okaz.Logic.*
import com.example.okaz.Repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderViewModel @ViewModelInject constructor(private val theAppRepoForAll: Repo): ViewModel() {
    val theOrder:MutableLiveData<PendingOrderForAdmin> = MutableLiveData()
    val whoOrdered:MutableLiveData<User> = MutableLiveData()
    val itemsInsideOrder:MutableLiveData<Map<String,OrderItem>> = MutableLiveData()
    val theProductsForRVAdapter:MutableLiveData<MutableList<CartProduct>> = MutableLiveData()
    val theitemdsForRv:MutableList<CartProduct> = mutableListOf()






    fun makeTheListForAdapter(themap:HashMap<String, OrderItem>)
    {


        for (i in themap)
        {
            FirebaseDatabase.getInstance().reference.child("Products").child(i.value!!.category!!).child(i.key!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val theProductDetails = snapshot.getValue(Product::class.java)

                        val theItemForList: CartProduct = (CartProduct(theProductDetails!!.image!!, theProductDetails.Name!!, theProductDetails.Category!!, theProductDetails.Price!!, i.value.quantity!!, theProductDetails.id!!))
                        theitemdsForRv.add(theItemForList)
                        //this is costy but we cant do anything else
                        theProductsForRVAdapter.value=theitemdsForRv
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        }



    }

    fun getThePendingOrderDetails(id:String)
    {
        FirebaseDatabase.getInstance().reference.child("PendingOrders").child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    FirebaseDatabase.getInstance().reference.child("PendingOrders").child(id).removeEventListener(this)
                    val theOrderForProgress = snapshot.getValue(PendingOrderForAdmin::class.java)
                    theOrder.value = theOrderForProgress
                    val theItems: MutableMap<String, OrderItem> = mutableMapOf()
                    FirebaseDatabase.getInstance().reference.child("PendingOrders").child(id)
                            .child("Items").addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        theItems.clear()
                                        for (i in snapshot.children) {
                                            val theOrderItem =i.getValue(OrderItem::class.java)
                                             theItems[i.key.toString()]=theOrderItem!!
                                        }
                                        itemsInsideOrder.value = theItems
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }

                            })

                    if (theOrder.value!!.Admin == "notYet") {

                        FirebaseDatabase.getInstance().reference.child("PendingOrders").child(id).child("Admin").setValue(FirebaseAuth.getInstance().currentUser!!.uid)
                    }

                    return
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun getTheOrderErDetails(theUser:String)
    {
        FirebaseDatabase.getInstance().reference.child("Users").child(theUser).addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    whoOrdered.value=snapshot.getValue(User::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}