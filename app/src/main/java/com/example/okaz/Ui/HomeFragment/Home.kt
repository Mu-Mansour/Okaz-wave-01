package com.example.okaz.Ui.HomeFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.okaz.Adapters.CategoryAdapter
import com.example.okaz.Adapters.CategoryProductAdapterForHome
import com.example.okaz.Adapters.HotProductAdapterForHome
import com.example.okaz.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.view.*
import kotlinx.android.synthetic.main.splash_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@AndroidEntryPoint
class Home : Fragment() {

    private val HotProductAdapter = HotProductAdapterForHome()
    private val categoryProductAdapter = CategoryProductAdapterForHome()
    private val categoryAdapter = CategoryAdapter(categoryProductAdapter)
val viewModel:HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getTheHotProducts()
        }
    }
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
        else
        {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.gettheUserDetails()
            }
            viewModel.theUser.observe(viewLifecycleOwner,{
                it?.let {
                    theView.profileImage.load(it.Image!!) {
                        scale(Scale.FIT)
                        transformations(CircleCropTransformation())
                        crossfade(true)
                        crossfade(300)

                    }
                    theView.userName.text="Hello ${it.Name!!}"
                }
            })

        }
        theView.retailCat.textSize=28f
        theView.retailCat.rotation=270f
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getTheCategoryProducts("Retail")
        }



        theView.fashionCat.setOnClickListener {
            theView.fashionCat.textSize=25f
            theView.beautyCat.textSize=16f
            theView.electronicsCat.textSize=16f
            theView.retailCat.textSize=16f
            YoYo.with(Techniques.Bounce).duration(1000).playOn(theView.fashionCat)
            theView.fashionCat.rotation=270f
            viewModel.getTheCategoryProducts("Fashion")


        }
        theView.beautyCat.setOnClickListener {
            theView.beautyCat.textSize=28f
            theView.fashionCat.textSize=16f
            theView.electronicsCat.textSize=16f
            theView.retailCat.textSize=16f
            YoYo.with(Techniques.Bounce).duration(1000).playOn(theView.beautyCat)
            theView.beautyCat.rotation=270f
            viewModel.getTheCategoryProducts("Beauty")
        }
        theView.electronicsCat.setOnClickListener {
            theView.electronicsCat.textSize=28f
            theView.fashionCat.textSize=16f
            theView.beautyCat.textSize=16f
            theView.retailCat.textSize=16f
            YoYo.with(Techniques.Bounce).duration(1000).playOn(theView.electronicsCat)
            theView.electronicsCat.rotation=270f
            viewModel.getTheCategoryProducts("Electronics")


        }
        theView.retailCat.setOnClickListener {
            theView.retailCat.textSize=28f
            theView.electronicsCat.textSize=16f
            theView.fashionCat.textSize=16f
            theView.beautyCat.textSize=16f
            YoYo.with(Techniques.Bounce).duration(1000).playOn(theView.retailCat)
            theView.retailCat.rotation=270f
            viewModel.getTheCategoryProducts("Retail")

        }


        theView.catRV.layoutManager= LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        theView.categoryRV.layoutManager= LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        theView.HotProductRV.layoutManager= LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        theView.catRV.adapter=categoryAdapter
        theView.categoryRV.adapter=categoryProductAdapter
        theView.HotProductRV.adapter=HotProductAdapter


        return theView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.theHotProductsForCsts.observe(viewLifecycleOwner, Observer {
            HotProductAdapter.submitTheList(it)
        })

        viewModel.theCategoryProductsForCsts.observe(viewLifecycleOwner,{
            categoryProductAdapter.submitTheList(it)
        })

        view.goToYourCart.setOnClickListener {
            findNavController().navigate(HomeDirections.actionHome2ToCart())
        }
        view.searchIcon.setOnClickListener {
            findNavController().navigate(HomeDirections.actionHome2ToSearchForProduct())
        }
        view.settings.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser==null)
            {
                findNavController().navigate(HomeDirections.actionHome2ToLogInCst())
            }
            else
            {
                findNavController().navigate(HomeDirections.actionHome2ToAcoountSettings())

            }

        }


    }

}