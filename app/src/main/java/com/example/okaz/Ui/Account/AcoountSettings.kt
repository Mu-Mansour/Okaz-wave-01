package com.example.okaz.Ui.Account

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.okaz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.acoount_settings_fragment.view.*
import kotlinx.android.synthetic.main.admin_home_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AcoountSettings : Fragment() {


    val viewModel:AcoountSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val theView = inflater.inflate(R.layout.acoount_settings_fragment, container, false)
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getTheUserImage().addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        lifecycleScope.launch(Dispatchers.Main) {
                            theView.profileImagefromAccountSettings.load(snapshot.value.toString()) {
                                scale(Scale.FIT)
                                transformations(CircleCropTransformation())
                                crossfade(true)
                                crossfade(300)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
        theView.changeTheImage.setOnClickListener {
            CropImage.activity().start(requireContext(), this)
        }
        theView.signOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(AcoountSettingsDirections.actionAcoountSettingsToHome2())
        }
        theView.gotoHome.setOnClickListener {
           findNavController().navigate(AcoountSettingsDirections.actionAcoountSettingsToHome2())
        }

        return theView
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&resultCode== Activity.RESULT_OK)
        {

            val result = CropImage.getActivityResult(data)

            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.storeAndUpdateImage(result.uri)
            }

        }
    }

}