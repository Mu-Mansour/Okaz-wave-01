package com.example.okaz.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.okaz.Logic.Pcategory
import com.example.okaz.R
import kotlinx.android.synthetic.main.category_rv_layout.view.*


class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val categories:Array<Pcategory> = arrayOf(Pcategory(1,"New Release ")
        ,Pcategory(2,"Hot"),
        Pcategory(3,"On  Sale"),
        Pcategory(4,"All"))


    inner class CategoryViewHolder(theView:View):RecyclerView.ViewHolder (theView)
    {
        var theType = theView.theType

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_rv_layout,parent,false ))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.theType.text=categories[position].name
    }

    override fun getItemCount(): Int {
return categories.size    }
}