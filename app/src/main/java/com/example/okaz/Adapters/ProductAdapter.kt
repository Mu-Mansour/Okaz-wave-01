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
//
    private val phonelink=R.drawable.phone
    private val carLink=R.drawable.car
    private val shampoLink=R.drawable.shampo
    private val laptopLink= R.drawable.lap
    private val foodLink=R.drawable.meat
    private var products:ArrayList<Product> = arrayListOf(Product("Samsung Note 20",20,22000.0,phonelink,"id1")
,           Product("Chevy Malipo",900,480000.0,carLink,"id2"),
        Product("Clear Shampo",94,87.0,shampoLink,"id3"),
        Product("Meat",209,135.5,foodLink,"id5") ,
        Product("Dell Laptop",7,18000.5,laptopLink,"id6")
    )

    fun submitTheList(productsList:MutableList<Product>)
    {
        products= productsList as ArrayList<Product>
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
        holder.theProductName.text=products[position].name
        holder.theProductchanges.text=":${products[position].quantity }\n:${products[position].price }"
        holder.theProductDetails.text="Quanity\nPrice"
    }

    override fun getItemCount(): Int {
return products.size    }
}