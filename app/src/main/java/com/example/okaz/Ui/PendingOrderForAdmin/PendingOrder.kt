package com.example.okaz.Ui.PendingOrderForAdmin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import coil.transform.CircleCropTransformation
import com.example.okaz.Adapters.CartAdapter
import com.example.okaz.Logic.OrderItem
import com.example.okaz.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.pending_order_fragment.*
import kotlinx.android.synthetic.main.pending_order_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PendingOrder : Fragment() {
    val theRgument:PendingOrderArgs by navArgs()
    lateinit var theProgress:ProgressDialog
    private  val viewModel: PendingOrderViewModel by viewModels()
    private  val theCartAdapter= CartAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){

            }
            viewModel.run { getThePendingOrderDetails(theRgument.OrderId) }

        }
        theProgress = ProgressDialog(requireContext()).apply {
            setMessage("Fetching")
            // window?.setBackgroundDrawable( ColorDrawable(Color.WHITE))
            setCancelable(false)
            setCanceledOnTouchOutside(false)
             show()
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val theView =inflater.inflate(R.layout.pending_order_fragment, container, false)

            viewModel.theOrder.observe(viewLifecycleOwner, {
                if (it != null) {
                    theView.adminOwner.text = if (it.Admin!! == FirebaseAuth.getInstance().currentUser!!.uid || it.Admin == "notYet") " Owner :You" else "Owner : Another Admin "
                    theView.toPerson.text = "Receiver: \nName :${it.theReceiver}\nAddress :${it.theAddress}\nPhone :${it.PhoneToContact}\nTotal Price:${it.price}\n"
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.getTheOrderErDetails(it.WhoOrdered!!)

                    }
                }
            })

            viewModel.itemsInsideOrder.observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    it?.let {
                        viewModel.makeTheListForAdapter(it as HashMap<String, OrderItem>)
                    }

                }
            })

            theView.gohomeAdminFromPending.setOnClickListener {
                 viewModel.theProductsForRVAdapter.value=null
                 viewModel.whoOrdered.value=null
                viewModel.theOrder.value=null
                viewModel.theitemdsForRv.clear()
                findNavController().navigate(PendingOrderDirections.actionPendingOrderToAdminHome())
            }

        return theView
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.whoOrdered.observe(viewLifecycleOwner,{
           it?.let {
                view.ordererImage.load(it.Image!!) {
                    scale(Scale.FIT)
                    transformations(CircleCropTransformation())
                    crossfade(true)
                    crossfade(300)
                }
                view.FromPerson.text = "Ordered By:\nName: ${it.Name}\nCredit: ${it.Credit}\nPhone: ${it.Phone}"
            }

        })

        viewModel.theProductsForRVAdapter.observe(viewLifecycleOwner, {
            it?.let {
                if (it.size>0)
                {
                        theCartAdapter.submitTheProducts(it)
                        theProgress.dismiss()
                }
            }
        })


        view.rvForItemsPendingAdmin.adapter=theCartAdapter
    }






}