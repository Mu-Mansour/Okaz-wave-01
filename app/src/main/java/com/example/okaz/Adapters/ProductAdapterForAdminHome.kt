package com.example.okaz.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.okaz.Logic.Product
import com.example.okaz.R
import kotlinx.android.synthetic.main.product_for_adminhome.view.*


class   ProductAdapterForAdminHome: RecyclerView.Adapter<ProductAdapterForAdminHome.ProducforAdmintViewHolder>() {

     var products:ArrayList<Product> = ArrayList()

    fun submitTheList(productsList:List<Product>)
    {
        products= productsList as ArrayList<Product>
        notifyDataSetChanged()
    }
    inner class ProducforAdmintViewHolder(theView:View):RecyclerView.ViewHolder (theView)
    {
        var theProductImage=theView.productPhoteForAdminHome
        var theProductName=theView.productnameForAdminHome
        var theProductchanges=theView.productDescreptionforAdminHome
       /* @SuppressLint("SetTextI18n")
        var theProductDetails=theView.details*/
        init {

                theView.setOnClickListener {
                    //itemView.findNavController().navigate(HomeDirections.actionHome2ToProductInfo(products[adapterPosition].id!!,"Hot"))
                }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProducforAdmintViewHolder {
        return ProducforAdmintViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_for_adminhome,parent,false ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProducforAdmintViewHolder, position: Int) {
        holder.theProductImage.load(products[position].image!!) {
            scale(Scale.FIT)
            transformations(RoundedCornersTransformation(30f))
            crossfade(true)
            crossfade(300)
        }
        holder.theProductName.text=products[position].Name
        holder.theProductchanges.text="${products[position].Category }\n${products[position].Price }"
    }



    override fun getItemCount(): Int {
        return products.size
    }

}