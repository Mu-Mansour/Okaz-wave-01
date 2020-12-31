package com.example.okaz.Ui.AdminLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.okaz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.admin_login_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AdminLogin : Fragment() {


   private val viewModel:AdminLoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val theView= inflater.inflate(R.layout.admin_login_fragment, container, false)

        theView.loginForAdmin.setOnClickListener {
            if (theView.EmialInputForAdmin.editText!!.text.isEmpty() || theView.passwordForAdmin.editText!!.text.isEmpty())
            {
                Toast.makeText(requireContext(), "check Your Entites", Toast.LENGTH_SHORT).show()
            }
            else {
                theView.adminProgressBar.visibility=View.VISIBLE
                viewModel.theEmail = theView.EmialInputForAdmin.editText!!.text.toString()
                viewModel.thePasswordl = theView.passwordForAdmin.editText!!.text.toString()
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.logInForAdmin()

                }
            }
        }
        viewModel.checking.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    findNavController().navigate(AdminLoginDirections.actionAdminLogin2ToAdminHome())
                }
                else
                {
                    viewModel.theError?.let {it1->
                        Toast.makeText(requireContext(), it1, Toast.LENGTH_SHORT).show()
                    }
                    theView.adminProgressBar.visibility=View.GONE
                }
            }

        })

        return theView
    }


}