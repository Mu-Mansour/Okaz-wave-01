package com.example.okaz.Ui.productFullInforToOrder

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.okaz.Logic.Product
import com.example.okaz.Logic.Utility
import com.example.okaz.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.product_info_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ProductInfo : Fragment() {

    private val viewModel:ProductInfoViewModel by viewModels()
    private val args:ProductInfoArgs by navArgs()
lateinit var theProgress:ProgressDialog

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val theView= inflater.inflate(R.layout.product_info_fragment, container, false)
            if (args.Streamer!="Admin")
            {
                if (args.Category == "Hot") {
                    viewModel.updateTheLiveHotProduct(args.productId)
                    viewModel.theProductLiveObject.observe(viewLifecycleOwner,{
                        it?.let {
                            theView.theProductImage.load(it.image) {
                                scale(Scale.FILL )
                                transformations(RoundedCornersTransformation(5f))
                                crossfade(true)
                                crossfade(300)
                            }
                            theView.ProductName.text =it.Name
                            theView.productPrice.text = it.Price
                            theView.productDescretopn.text = it.Description
                        }
                    })
                }
                else
                {
                    viewModel.updateTheLiveProduct(args.productId,args.Category)
                    viewModel.theProductLiveObject.observe(viewLifecycleOwner,{
                        it?.let{
                            theView.theProductImage.load(it.image) {
                                scale(Scale.FIT)
                                transformations(RoundedCornersTransformation(5f))
                                crossfade(true)
                                crossfade(300)
                            }
                            theView.ProductName.text =it.Name
                            theView.productPrice.text = it.Price
                            theView.productDescretopn.text =
                                    it.Description
                        }
                    })

                }

            }
        else
            {
                viewModel.updateTheLiveProduct(args.productId,args.Category)
                viewModel.theProductLiveObject.observe(viewLifecycleOwner,{
                    it?.let{
                        theView.theProductImage.load(it.image) {
                            scale(Scale.FILL)
                            // transformations(RoundedCornersTransformation(5f))
                            crossfade(true)
                            crossfade(300)
                        }
                        theView.ProductName.visibility=View.GONE
                        theView.productPrice.visibility=View.GONE
                        theView.productDescretopn.visibility=View.GONE
                        theView.addMorefromProduct.visibility=View.GONE
                        theView.minusFromProduct.visibility=View.GONE
                        theView.quantityFromProducts.visibility=View.GONE
                        theView.editNameAdmin.visibility=View.VISIBLE
                        theView.editPriceAdmin.visibility=View.VISIBLE
                        theView.editDescreptionAdmin.visibility=View.VISIBLE
                        theView.DeleteForAdmin.visibility=View.VISIBLE
                        theView.addToCartFromProduct.text="Update"
                        theView.editNameAdmin.setText(it.Name)
                        theView.editPriceAdmin.setText(it.Price)
                        theView.editDescreptionAdmin.setText(it.Description)




                    }
                })


            }


        theView.DeleteForAdmin.setOnClickListener {
            theProgress = ProgressDialog(requireContext()).apply {
                setMessage("Deleting")
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                show()
            }
            viewModel.deleteTheProduct()
            viewModel.TaskCompletedForAdmin.observe(viewLifecycleOwner, {
                if (it) {
                    theProgress.dismiss()
                    Toast.makeText(requireContext(), "Deleted Successfully ", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(ProductInfoDirections.actionProductInfoToAdminHome())
                }
            })
        }


        theView.addMorefromProduct.setOnClickListener {
            viewModel.theQuantity++
            theView. quantityFromProducts.text=viewModel.theQuantity.toString()
        }
        theView.minusFromProduct.setOnClickListener {
            if (  viewModel.theQuantity==0)
            {
                Toast.makeText(requireContext(), "Your Quantity is 0 already", Toast.LENGTH_SHORT).show()
            }
            else
            {
                viewModel.theQuantity--
                theView. quantityFromProducts.text=viewModel.theQuantity.toString()
            }

        }
        theView.gobackToHome.setOnClickListener {
            if (args.Streamer!="Admin")
            {
                findNavController().navigate(ProductInfoDirections.actionProductInfoToHome2())
                viewModel.theProductLiveObject.value=null
            }
            else
            {
                findNavController().navigate(ProductInfoDirections.actionProductInfoToAdminHome())
                viewModel.theProductLiveObject.value=null
            }
        }
        theView.addToCartFromProduct.setOnClickListener {
            if (theView.addToCartFromProduct.text!="Update")
            {
                if (viewModel.theQuantity> 0)
                {
                    Utility.theOrder[viewModel.theProductLiveObject.value!!] = (viewModel.theQuantity * viewModel.theProductLiveObject.value!!.Price!!.toDouble()).toString()
                    Toast.makeText(requireContext(), "${viewModel.theProductLiveObject.value!!.Name!!} with Quantity ${ viewModel.theQuantity} Added To Tour Cart ", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(ProductInfoDirections.actionProductInfoToHome2())
                }
                else
                {
                    Toast.makeText(requireContext(), "Please Define a Quantity ", Toast.LENGTH_SHORT).show()

                }
            }
            else
            {

                if ( theView.editNameAdmin.text.isEmpty() || theView.editPriceAdmin.text.isEmpty() || theView.editDescreptionAdmin.text.isEmpty())
                {
                    Toast.makeText(requireContext(), "CheckEntities", Toast.LENGTH_SHORT).show()
                }
                else
                {
                   theProgress = ProgressDialog(requireContext()).apply {
                    setMessage("Updating")
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                    show()
                }

                    viewModel.theNewName=theView.editNameAdmin.text.toString().toLowerCase(Locale.ROOT)
                    viewModel.theNewDescreption=theView.editDescreptionAdmin.text.toString().toLowerCase(Locale.ROOT)
                    viewModel. theNewPrice=theView.editPriceAdmin.text.toString().toLowerCase(Locale.ROOT)
                    theView.editNameAdmin.visibility=View.GONE
                    theView.editPriceAdmin.visibility=View.GONE
                    theView.editDescreptionAdmin.visibility=View.GONE
                    theView.DeleteForAdmin.visibility=View.GONE
                    viewModel.updateThatProductForAdmin()
                    viewModel.TaskCompletedForAdmin.observe(viewLifecycleOwner, {
                        if (it) {
                            theProgress.dismiss()
                            Toast.makeText(requireContext(), "Updated Successfully ", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(ProductInfoDirections.actionProductInfoToAdminHome())
                        }
                    })
                }


            }



        }
        return theView
    }

}