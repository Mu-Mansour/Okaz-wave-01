package com.example.okaz.Ui.Cart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.okaz.Adapters.CartAdapter
import com.example.okaz.Logic.Utility
import com.example.okaz.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.cart_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Cart : Fragment() {


    private  val viewModel: CartViewModel by viewModels()
    private  val theAdapterForCart=CartAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Utility.theOrder.size>0)
        {
            Utility.cartPrice .value=0.0
            viewModel.makeTheListFromUtitity()

        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val theView= inflater.inflate(R.layout.cart_fragment, container, false)

        Utility.cartPrice.value?.let {
            theView.cartProductsRV.visibility=View.VISIBLE
            theView.noOrders.visibility=View.GONE
            theView.priceTVForCart.visibility=View.VISIBLE
        } ?: run {
            theView.cartProductsRV.visibility = View.GONE
            theView.priceTVForCart.visibility = View.GONE
            theView.noOrders.visibility = View.VISIBLE
        }

        theView.goToHomeFromCart.setOnClickListener {
            findNavController().navigate(CartDirections.actionCartToHome2())
        }
        return theView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.theProductsForCart.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty())
            {
                theAdapterForCart.submitTheProducts(it)

            }
        })
        view.cartProductsRV.adapter=theAdapterForCart
        Utility.cartPrice.observe(viewLifecycleOwner, Observer {
            view.priceTVForCart.text="Price $it EGP "
            if (it==0.0)
            {
                view   .cartProductsRV.visibility=View.GONE
                view.noOrders.visibility=View.VISIBLE
                Utility.theOrder.clear()
            }
        })
        ItemTouchHelper(theSwipper).attachToRecyclerView(view.cartProductsRV)
    }


    private val theSwipper = object : ItemTouchHelper.SimpleCallback ( 0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            AlertDialog.Builder(requireContext())
                .setMessage("You  are about Deleting This Item")
                .setTitle("Confirmation ")
                .setPositiveButton(" Confirm ", DialogInterface.OnClickListener { _, _ ->
                    Utility.cartPrice.value =
                        Utility.cartPrice.value?.minus(theAdapterForCart.Products[viewHolder.adapterPosition].quantity.toDouble())
                    theAdapterForCart.Products.removeAt(viewHolder.adapterPosition)
                    theAdapterForCart.notifyDataSetChanged()

                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_SHORT).show()
                }).show()

            theAdapterForCart.notifyDataSetChanged()
        }
    }
}