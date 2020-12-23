package com.example.okaz.Ui.HomeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.okaz.Adapters.CategoryAdapter
import com.example.okaz.Adapters.ProductAdapter
import com.example.okaz.R
import kotlinx.android.synthetic.main.home_fragment.view.*

class Home : Fragment(R.layout.home_fragment) {
private val categoryAdapter = CategoryAdapter()
private val productAdapter = ProductAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val theView = inflater.inflate(R.layout.home_fragment,container,false)


        theView.catRV.layoutManager= LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        theView.ProductRV.layoutManager= LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        theView.catRV.adapter=categoryAdapter
        theView.ProductRV.adapter=productAdapter
        return theView
    }

}