package com.example.okaz.Ui.SearchForCst

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.okaz.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchForProduct : Fragment() {


    private  val viewModel: SearchForProductViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val theView=inflater.inflate(R.layout.search_for_product_fragment, container, false)

        return theView
    }



}