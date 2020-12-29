package com.example.okaz.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.okaz.R
import com.example.okaz.Ui.AdminHome.AdminHomeDirections
import kotlinx.android.synthetic.main.pending_orders_rv_foradmin.view.*


class   PendingOrdersAdapterForAdminHome: RecyclerView.Adapter<PendingOrdersAdapterForAdminHome.PendingItemViewHolder>() {

     var orders:ArrayList<String> = ArrayList()

    fun submitTheList(productsList:List<String>)
    {
        orders= productsList as ArrayList<String>
        notifyDataSetChanged()
    }
    inner class PendingItemViewHolder(theView:View):RecyclerView.ViewHolder (theView)
    {
        val theOrderView=theView.thePendingOrderForAdmin

        init {

                theView.setOnClickListener {
                    if (adapterPosition!= RecyclerView.NO_POSITION)
                    {
                        itemView.findNavController().navigate(AdminHomeDirections.actionAdminHomeToPendingOrder(orders[adapterPosition]))
                    }

                }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingItemViewHolder {
        return PendingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pending_orders_rv_foradmin,parent,false ))
    }


    override fun onBindViewHolder(holder: PendingItemViewHolder, position: Int) {
      holder.theOrderView.text=orders[position]
    }



    override fun getItemCount(): Int {
        return orders.size
    }

}