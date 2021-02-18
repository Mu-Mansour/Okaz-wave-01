package com.example.okaz.Ui.logInCst

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.okaz.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.log_in_cst_fragment.*
import kotlinx.android.synthetic.main.log_in_cst_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LogInCst : Fragment() {

    private val theVM: LogInCstViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val theView = inflater.inflate(R.layout.log_in_cst_fragment, container, false)

        return theView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendTheMessage.setOnClickListener {
            if (inputName.editText!!.text.isEmpty() || inputPhone.editText!!.text.isEmpty()) {
                Toast.makeText(requireContext(), "Check ur Entities ", Toast.LENGTH_SHORT).show()
            } else {
                theVM.theName = inputName.editText!!.text.toString()
                theVM.thePhoneNumber = inputPhone.editText!!.text.toString()
                lifecycleScope.launch {
                    inputPhone.visibility = View.GONE
                    inputName.visibility = View.GONE
                    sendTheMessage.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    msgInput.visibility = View.VISIBLE
                    verifyBTN.visibility = View.VISIBLE
                    getTheViewsBack.visibility = View.VISIBLE
                    theVM.takeDataAndSignUp(requireActivity())
                }
            }
        }

        verifyBTN.setOnClickListener {
            if (msgInput.editText!!.text.isEmpty()) {
                Toast.makeText(requireContext(), "Check ur Entities ", Toast.LENGTH_SHORT).show()
            }
            else
            {


                theVM.theUthKey = msgInput.editText!!.text.toString()
                theVM.theCodeSent?.let {
                    theVM.verifyTheNewCustomer()?.addOnSuccessListener {
                        lifecycleScope.launch(Dispatchers.IO) {
                            theVM. checkToCreate()
                            withContext(Dispatchers.Main){
                                theVM.userExist.observe(viewLifecycleOwner, {
                                    it?.let {
                                        if (it) {
                                            Toast.makeText(requireContext(), "Welcome Back", Toast.LENGTH_SHORT).show()
                                            findNavController().navigate(LogInCstDirections.actionLogInCstToHome2())
                                        }
                                        else
                                        {
                                            Toast.makeText(requireContext(), "Thank You For Joining Okaz", Toast.LENGTH_SHORT).show()
                                            findNavController().navigate(LogInCstDirections.actionLogInCstToHome2())
                                        }
                                    }
                                })
                            }
                        }
                    }?.addOnFailureListener {
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                } ?: Toast.makeText(requireContext(), "Please wait till You receive a message", Toast.LENGTH_SHORT).show()

            }
               }
        adminLogin.setOnClickListener {
            AlertDialog.Builder(requireContext())
                    .setMessage("This is For Admin log in\nPlease confirm if u r an Admin..")
                    .setTitle("Admin Log In")
                    .setPositiveButton(" Confirm", DialogInterface.OnClickListener { _, _ ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            withContext(Dispatchers.Main) {
                                findNavController().navigate(LogInCstDirections.actionLogInCstToAdminLogin2())
                            }
                        }


                    }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                        Toast.makeText(requireContext(), "Canceled ..", Toast.LENGTH_SHORT).show()
                    }).show()
        }
        getTheViewsBack.setOnClickListener {
            inputPhone.visibility = View.VISIBLE
            inputName.visibility = View.VISIBLE
            sendTheMessage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            getTheViewsBack.visibility = View.GONE
            msgInput.visibility = View.GONE
            verifyBTN.visibility = View.GONE


        }
    }
}



