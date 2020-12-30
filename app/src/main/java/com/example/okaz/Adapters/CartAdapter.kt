package com.example.okaz.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.okaz.Logic.CartProduct
import com.example.okaz.Logic.OrderItem
import com.example.okaz.Logic.Utility
import com.example.okaz.R
import kotlinx.android.synthetic.main.cart_rv_layout.view.*


class CartAdapter: RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

     var Products:ArrayList<CartProduct> = ArrayList()

    fun submitTheProducts(pros:List<CartProduct>)
    {
        Products=pros as ArrayList<CartProduct>
        notifyDataSetChanged()
    }
    fun makeTheOrder()
    {
        Utility.theFinalOrder= hashMapOf()
        for (i in Products)
        {
            Utility.theFinalOrder?.set(i.id, OrderItem(i.cat, (i.quantity)))
        }
    }
    inner class CartViewHolder(theView:View):RecyclerView.ViewHolder (theView)
    {
        var theDetailsForCart = theView.detailsOfProductForCart
        var theQuantityForCart = theView.quantityForCartRV
        var theImageFromCart= theView.productImageForCartRV
        var addOne= theView.addFromCart
        var MinusOne= theView.minusFromCart
        init {

                addOne.setOnClickListener {
                    if (adapterPosition!=RecyclerView.NO_POSITION )
                    {

                        Products[adapterPosition].quantity= (Products[adapterPosition].quantity.toDouble() +Products[adapterPosition].price.toDouble()).toString()
                        Utility.cartPrice.value =
                            Utility.cartPrice.value?.plus(Products[adapterPosition].price.toDouble())
                        notifyDataSetChanged()
                    }

                }
                MinusOne.setOnClickListener {
                    if (adapterPosition!=RecyclerView.NO_POSITION &&Products[adapterPosition].quantity.toDouble()!=Products[adapterPosition].price.toDouble())
                    {
                        Products[adapterPosition].quantity= (Products[adapterPosition].quantity.toDouble() -Products[adapterPosition].price.toDouble()).toString()
                        Utility.cartPrice.value =
                            Utility.cartPrice.value?.minus(Products[adapterPosition].price.toDouble())
                        notifyDataSetChanged()
                    }
                }
            }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_rv_layout,parent,false ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.theDetailsForCart.text="Namee: ${Products[position].name}\nCategory: ${Products[position].cat}\nPrice: ${Products[position].price}\nTotal Amount \nEGP: ${Products[position].quantity}"
        holder.theQuantityForCart.text= "${(Products[position].quantity.toDouble() / Products[position].price.toDouble() ).toInt()}"
        holder.theImageFromCart.load(Products[position].image){
            scale(Scale.FILL)
            transformations(RoundedCornersTransformation(5f))
            crossfade(true)
            crossfade(300)
        }
    }

    override fun getItemCount(): Int {
return Products.size    }
}