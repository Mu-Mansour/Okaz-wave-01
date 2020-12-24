package com.example.okaz.Ui.HomeFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.okaz.Adapters.CategoryAdapter
import com.example.okaz.Adapters.ProductAdapter
import com.example.okaz.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_fragment.view.*

class Home : Fragment(R.layout.home_fragment) {
private val categoryAdapter = CategoryAdapter()
private val productAdapter = ProductAdapter()
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val theView = inflater.inflate(R.layout.home_fragment,container,false)

        if (FirebaseAuth.getInstance().currentUser==null)
        {
            theView.profileImage.load("https://firebasestorage.googleapis.com/v0/b/okaz-8c8c3.appspot.com/o/defaultProfileImage%2FdefaultUsers.png?alt=media&token=6ffe3516-4e23-4110-a7df-8bc923b2a08f\n") {
                scale(Scale.FIT)
                transformations(CircleCropTransformation())
                crossfade(true)
                crossfade(300)

            }
            theView.userName.text="Hello Guest"
        }
        theView.catRV.layoutManager= LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        theView.ProductRV.layoutManager= LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        theView.catRV.adapter=categoryAdapter
        theView.ProductRV.adapter=productAdapter
        return theView
    }

}