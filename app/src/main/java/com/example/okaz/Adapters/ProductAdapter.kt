package com.example.okaz.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.okaz.Logic.Pcategory
import com.example.okaz.Logic.Product
import com.example.okaz.R
import kotlinx.android.synthetic.main.category_rv_layout.view.*
import kotlinx.android.synthetic.main.product_rv_layout.view.*


class   ProductAdapter: RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

     var products:ArrayList<Product> = ArrayList()

    fun submitTheList(productsList:List<Product>)
    {
        products= productsList as ArrayList<Product>
        notifyDataSetChanged()
    }
    inner class ProductViewHolder(theView:View):RecyclerView.ViewHolder (theView)
    {
        var theProductImage=theView.productimage
        var theProductName=theView.productNmae
        var theProductchanges=theView.theObjectDetails
        @SuppressLint("SetTextI18n")
        var theProductDetails=theView.details

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_rv_layout,parent,false ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.theProductImage.load(products[position].image!!) {
            scale(Scale.FIT)
            transformations(RoundedCornersTransformation(30f))
            crossfade(true)
            crossfade(300)
        }
        holder.theProductName.text=products[position].Name
        holder.theProductchanges.text=":${products[position].Category }\n:${products[position].Price }"
        holder.theProductDetails.text="Category\nPrice"
    }



    override fun getItemCount(): Int {
        return products.size
    }

}