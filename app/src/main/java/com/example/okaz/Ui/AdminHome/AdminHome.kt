package com.example.okaz.Ui.AdminHome

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
class AdminHome : Fragment() {


    private  val viewModel: AdminHomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.admin_home_fragment, container, false)
    }



}