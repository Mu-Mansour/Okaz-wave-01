package com.example.okaz.Ui.Cart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
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
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.okaz.Adapters.CartAdapter
import com.example.okaz.Logic.Utility
import com.example.okaz.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.admin_home_fragment.view.*
import kotlinx.android.synthetic.main.cart_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            theView.recieverFromCart.visibility = View.GONE
            theView.adressCart.visibility = View.GONE
            theView.dphoneFromCart.visibility = View.GONE
            theView.Confirmation.visibility = View.GONE
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
                view.cartProductsRV.visibility = View.GONE
                view.priceTVForCart.visibility = View.GONE
                view.recieverFromCart.visibility = View.GONE
                view.adressCart.visibility = View.GONE
                view.dphoneFromCart.visibility = View.GONE
                view.Confirmation.visibility = View.GONE
                view.noOrders.visibility = View.VISIBLE
                Utility.theOrder.clear()
            }
        })
        view.GoToLogInImage.setOnClickListener {
            findNavController().navigate(CartDirections.actionCartToLogInCst())
        }
        view.Confirmation.setOnClickListener {
            if (view.recieverFromCart.editText!!.text.isEmpty() ||view.adressCart.editText!!.text.isEmpty() ||view.dphoneFromCart.editText!!.text.isEmpty())
            {
                Toast.makeText(requireContext(), "Enter Your Shipment Details", Toast.LENGTH_SHORT).show()
            }
            else if (FirebaseAuth.getInstance().currentUser==null)
            {
                Toast.makeText(requireContext(), "Please Log In First", Toast.LENGTH_SHORT).show()
                view.GoToLogInImage.visibility=View.VISIBLE
                view.gotoLogin.visibility=View.VISIBLE
                YoYo.with(Techniques.Shake).playOn(view.GoToLogInImage)

            }
            else
            {
                val theProgress = ProgressDialog(requireContext()).apply {
                    setMessage("Confirming Order")
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                    show()
                }
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.PhoneToContact = view.dphoneFromCart.editText!!.text.toString()
                    viewModel.theAdress = view.adressCart.editText!!.text.toString()
                    viewModel.theReciver = view.recieverFromCart.editText!!.text.toString()
                    theAdapterForCart.makeTheOrder()
                    viewModel.updateToDataBaseOrder(theProgress)

                }
            }

        }
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