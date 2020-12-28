package com.example.okaz.Ui.productFullInforToOrder

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

@AndroidEntryPoint
class ProductInfo : Fragment() {

val viewModel:ProductInfoViewModel by viewModels()

    val args:ProductInfoArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val theView= inflater.inflate(R.layout.product_info_fragment, container, false)

        lifecycleScope.launch(Dispatchers.IO) {
            if (args.Category == "Hot") {

                viewModel.getTheProductFromHot(args.productId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                viewModel.theProduct = snapshot.getValue(Product::class.java)
                                viewModel.getTheProductFromCategories(args.productId, args.Category)
                                    .removeEventListener(this)
                                lifecycleScope.launch(Dispatchers.Main) {
                                    theView.theProductImage.load(viewModel.theProduct!!.image) {
                                        scale(Scale.FILL)
                                        transformations(RoundedCornersTransformation(5f))
                                        crossfade(true)
                                        crossfade(300)
                                    }
                                    theView.ProductName.text = viewModel.theProduct!!.Name
                                    theView.productPrice.text = viewModel.theProduct!!.Price
                                    theView.productDescretopn.text =
                                        viewModel.theProduct!!.Description
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
            } else {
                viewModel.getTheProductFromCategories(args.productId, args.Category)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                viewModel.theProduct = snapshot.getValue(Product::class.java)
                                viewModel.getTheProductFromCategories(args.productId, args.Category)
                                    .removeEventListener(this)
                                lifecycleScope.launch(Dispatchers.Main) {
                                    theView.theProductImage.load(viewModel.theProduct!!.image) {
                                        scale(Scale.FILL)
                                        transformations(RoundedCornersTransformation(5f))
                                        crossfade(true)
                                        crossfade(300)
                                    }
                                    theView.ProductName.text = viewModel.theProduct!!.Name
                                    theView.productPrice.text = viewModel.theProduct!!.Price
                                    theView.productDescretopn.text =
                                        viewModel.theProduct!!.Description
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
            }
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
        theView.addToCartFromProduct.setOnClickListener {
            if (viewModel.theQuantity> 0)
            {
                Utility.theOrder[viewModel.theProduct!!] = (viewModel.theQuantity * viewModel.theProduct!!.Price!!.toDouble()).toString()
                Toast.makeText(requireContext(), "${viewModel.theProduct!!.Name!!} with Quantity ${ viewModel.theQuantity} Added To Tour Cart ", Toast.LENGTH_SHORT).show()
                findNavController().navigate(ProductInfoDirections.actionProductInfoToHome2())
            }
            else
            {
                Toast.makeText(requireContext(), "Please Define a Quantity ", Toast.LENGTH_SHORT).show()

            }

        }
        return theView
    }

}