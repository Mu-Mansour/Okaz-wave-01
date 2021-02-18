package com.example.okaz.Ui.AdminHome

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.okaz.Adapters.PendingOrdersAdapterForAdminHome
import com.example.okaz.Adapters.ProductAdapterForAdminHome
import com.example.okaz.R
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.admin_home_fragment.*
import kotlinx.android.synthetic.main.admin_home_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AdminHome : Fragment() {
private val theProductAdapter= ProductAdapterForAdminHome()
private val theOrdersAdapter= PendingOrdersAdapterForAdminHome()

    private  val viewModel: AdminHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getTheHotProducts()
            viewModel.getThePendingOrders()

        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val theView= inflater.inflate(R.layout.admin_home_fragment,container,false)

        theView.fashionimageforadmin.setOnClickListener {
            viewModel.theCategory="Fashion"
            theView.theCatChosen.text= "Fashion"
            YoYo.with(Techniques.Shake).playOn(theView.fashionimageforadmin)
        }
        theView.beautyadmin.setOnClickListener {
            viewModel.theCategory="Beauty"
            theView.theCatChosen.text= "Beauty"
            YoYo.with(Techniques.Shake).playOn(  theView.beautyadmin)
        }
        theView.electronicsAdmin.setOnClickListener {
            viewModel.theCategory="Electronics"
            theView.theCatChosen.text= "Electronics"
            YoYo.with(Techniques.Shake).playOn(    theView.electronicsAdmin)
        }
        theView.retailimageAdmin.setOnClickListener {
            viewModel.theCategory="Retail"
            theView.theCatChosen.text= "Retail"
            YoYo.with(Techniques.Shake).playOn(theView.retailimageAdmin)

        }
        theView.ProductImage.setOnClickListener {
            CropImage.activity().start(requireContext(), this)
        }
        theView.SearchfromAdminHome.setOnClickListener {
            findNavController().navigate(AdminHomeDirections.actionAdminHomeToSearchForProduct())
        }


        theView.addToSelectedCatAdmin.setOnClickListener {
            if (theView.theProductDescAdmin.text.isEmpty() || theView.theproductPrice.text.isEmpty() || theView.theproductNmaeInput.text.isEmpty() || viewModel.theUriForImage == null || viewModel.theCategory == null) {
                Toast.makeText(requireContext(), "Check Your Entities", Toast.LENGTH_SHORT).show()
            } else
            {
                viewModel.theProductDescription = theView.theProductDescAdmin.text.toString()
                viewModel.theProductNmae = theView.theproductNmaeInput.text.toString()
                viewModel.theProductPrice = theView.theproductPrice.text.toString()
                lifecycleScope.launch {
                    val theProgress = ProgressDialog(requireContext()).apply {
                        setMessage("Uploading..")
                        setCancelable(false)
                        setCanceledOnTouchOutside(false)
                        show()
                    }
                    withContext(Dispatchers.IO) {
                        viewModel.mkaeTheProductMapAndUploadIt().await()
                        withContext(Dispatchers.Main){
                            viewModel.jobIsDone.observe(viewLifecycleOwner, {
                                it?.let {
                                    if (it) {
                                        theView.theproductNmaeInput.text.clear()
                                        theView.theproductPrice.text.clear()
                                        theView.theProductDescAdmin.text.clear()
                                        theView.ProductImage.load(R.drawable.ic_baseline_camera_alt_24)
                                        theProgress.dismiss()
                                        Toast.makeText(requireContext(), "Added Successfully ", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            })
                        }


                    }
                }

            }
        }
        theView.addHotAdmin.setOnClickListener {

            if (theView.theProductDescAdmin.text.isEmpty() || theView.theproductPrice.text.isEmpty() || theView.theproductNmaeInput.text.isEmpty() || viewModel.theUriForImage == null || viewModel.theCategory == null) {
                Toast.makeText(requireContext(), "Check Your Entities", Toast.LENGTH_SHORT).show()
            } else if (theProductAdapter.products.size > 25) {
                Toast.makeText(requireContext(), "Your Hot Product List Is full", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.theProductDescription = theView.theProductDescAdmin.text.toString()
                viewModel.theProductNmae = theView.theproductNmaeInput.text.toString()
                viewModel.theProductPrice = theView.theproductPrice.text.toString()
                lifecycleScope.launch {
                    val theProgress = ProgressDialog(requireContext()).apply {
                        setMessage("Uploading..")
                        setCancelable(false)
                        setCanceledOnTouchOutside(false)
                        show()
                    }
                    withContext(Dispatchers.IO) {
                        viewModel.mkaeTheHotProductMapAndUploadIt().await()
                        withContext(Dispatchers.Main){
                            viewModel.jobIsDone.observe(viewLifecycleOwner, {
                                it?.let {
                                    if (it) {
                                        theView.theproductNmaeInput.text.clear()
                                        theView.theproductPrice.text.clear()
                                        theView.theProductDescAdmin.text.clear()
                                        theView.ProductImage.load(R.drawable.ic_baseline_camera_alt_24)
                                        theProgress.dismiss()
                                        Toast.makeText(requireContext(), "Added Successfully ", Toast.LENGTH_SHORT).show()

                                    }
                                }
                            })
                        }
                    }
                }
            }

        }
        return theView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.theHotProducts.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                theProductAdapter.submitTheList(it)
                view.swipeableTV.visibility = View.VISIBLE
                view.swipimage.visibility = View.VISIBLE
            }
        })
        viewModel.thePendingOrders.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty())
            {
                view.PendingOrdersCount.visibility=View.VISIBLE
                view.PendingOrdersCount.text="Pending Orders Count is : ${it.size}"
                theOrdersAdapter.submitTheList(it)

                }


        })
        view.rvForAdminHotProducts.adapter=theProductAdapter
        view.PendingOrdersRV.adapter=theOrdersAdapter




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&resultCode== Activity.RESULT_OK)
        {

            val result = CropImage.getActivityResult(data)

            ProductImage.load(result.uri){
                crossfade(true)
                crossfade(300)
                scale(Scale.FILL)
                transformations(RoundedCornersTransformation(5f))
            }
            viewModel.theUriForImage=result.uri
        }
    }






}