package com.example.okaz.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.okaz.Logic.Pcategory
import com.example.okaz.R
import kotlinx.android.synthetic.main.category_rv_layout.view.*


class CategoryAdapter(val theProductAdapter:CategoryProductAdapterForHome): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val categories:Array<Pcategory> = arrayOf(Pcategory(1,"Newest")
        ,Pcategory(2,"Oldest"),
        Pcategory(3,"Lowest Price"),
        Pcategory(4,"Highest Price"))
    private var itsFRomOldToNew=true



    inner class CategoryViewHolder(theView:View):RecyclerView.ViewHolder (theView)
    {
        var theType = theView.theType

        init {
            theView.setOnClickListener {
                if (adapterPosition!=RecyclerView.NO_POSITION && theProductAdapter.products.size>0)
                {
                    if (theView.theType.text=="Oldest")
                    {
                        swapBetweenOldAndNew()
                        theProductAdapter.notifyDataSetChanged()
                    }
                    else if (theView.theType.text=="Newest") {
                        swapBetweenOldAndNew()
                        theProductAdapter.notifyDataSetChanged()
                    }
                    else if (theView.theType.text=="Lowest Price")
                    {
                        sortByPricelLowToHigh()
                        theProductAdapter.notifyDataSetChanged()
                    }
                    else if (theView.theType.text=="Highest Price")
                    {
                        sortByPriceUpToDown()
                        theProductAdapter.notifyDataSetChanged()

                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_rv_layout,parent,false ))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.theType.text=categories[position].name
    }

    fun sortByPriceUpToDown()
    {
        theProductAdapter.products.sortByDescending {
            it.Price.toString().toDouble()
        }
    }
    fun sortByPricelLowToHigh()
    {
        theProductAdapter.products.sortBy {
            it.Price.toString().toDouble()
        }
    }
    fun swapBetweenOldAndNew()
    {
        if (itsFRomOldToNew)
        {
            theProductAdapter.products.reverse()
            itsFRomOldToNew=false

        }
        else
        {
            theProductAdapter.products.reverse()
            itsFRomOldToNew=true
        }

    }

    override fun getItemCount(): Int {
return categories.size    }
}