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
                    viewModel.logInForAdmin().addOnCompleteListener {
                        if (it.isSuccessful) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                viewModel.refrenceToAdmins().addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            lifecycleScope.launch(Dispatchers.Main) {
                                                findNavController().navigate(AdminLoginDirections.actionAdminLogin2ToAdminHome())
                                            }
                                        } else {
                                            lifecycleScope.launch(Dispatchers.IO) {
                                                FirebaseAuth.getInstance().signOut()
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(requireContext(), "Un Authorized Login", Toast.LENGTH_SHORT).show()
                                                    requireActivity().finish()
                                                }
                                            }
                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }

                                })

                            }
                        } else {
                            lifecycleScope.launch(Dispatchers.Main) {
                                theView.adminProgressBar.visibility=View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    "${it.exception!!.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
        return theView
    }


}