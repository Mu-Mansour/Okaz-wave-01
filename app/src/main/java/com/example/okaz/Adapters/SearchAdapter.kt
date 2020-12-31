package com.example.okaz.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.okaz.Logic.ItemForSearchTotall
import com.example.okaz.R
import com.example.okaz.Ui.AdminHome.AdminHomeDirections
import com.example.okaz.Ui.SearchForCst.SearchForProductDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.for_searc_adapter.view.*


class   SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchItemViewHolder>() {

     var items:ArrayList<ItemForSearchTotall> = ArrayList()


    fun submitTheList(productsList:List<ItemForSearchTotall>)
    {
        items= productsList as ArrayList<ItemForSearchTotall>
        notifyDataSetChanged()
    }
    inner class SearchItemViewHolder(theView:View):RecyclerView.ViewHolder (theView)
    {
        val theSearchedItem=theView.itemForSearch

        init {

                theView.setOnClickListener {
                    if (adapterPosition!= RecyclerView.NO_POSITION)
                    {
                        if (FirebaseAuth.getInstance().currentUser==null)
                        {
                            itemView.findNavController().navigate(SearchForProductDirections.actionSearchForProductToProductInfo(items[adapterPosition].id!!,items[adapterPosition].category!!))

                        }
                        else
                        {
                            FirebaseDatabase.getInstance().reference.child("Admins")
                                    .child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object :ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists())
                                            {
                                                FirebaseDatabase.getInstance().reference.child("Admins")
                                                        .child(FirebaseAuth.getInstance().currentUser!!.uid).removeEventListener(this)
                                                itemView.findNavController().navigate(SearchForProductDirections.actionSearchForProductToProductInfo(items[adapterPosition].id!!,items[adapterPosition].category!!,"Admin"))

                                            }
                                            else
                                            {
                                                itemView.findNavController().navigate(SearchForProductDirections.actionSearchForProductToProductInfo(items[adapterPosition].id!!,items[adapterPosition].category!!))

                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }

                                    })
                        }
                    }

                }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        return SearchItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.for_searc_adapter,parent,false ))
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
      holder.theSearchedItem.text="${items[position].name } From ${items[position].category}"
    }



    override fun getItemCount(): Int {
        return items.size
    }

}