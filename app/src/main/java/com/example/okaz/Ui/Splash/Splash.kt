package com.example.okaz.Ui.Splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.okaz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.splash_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint
class Splash : Fragment() {
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val theView = inflater.inflate(R.layout.splash_fragment, container, false)


        return theView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
if (FirebaseAuth.getInstance().currentUser!= null)
{
    YoYo.with(Techniques.FadeIn).duration(2000).playOn(imageView)
        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            viewModel.checkMyRefrence().addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        viewModel.checkMyRefrence().removeEventListener(this)
                        FirebaseAuth.getInstance().signOut()
                        lifecycleScope.launch(Dispatchers.Main) {
                            findNavController().navigate(SplashDirections.actionSplashToHome2())
                        }
                    } else {
                        viewModel.checkMyRefrence().removeEventListener(this)
                        lifecycleScope.launch(Dispatchers.Main) {
                            findNavController().navigate(SplashDirections.actionSplashToHome2())
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


        }
    }
        else
        {
    lifecycleScope.launch(Dispatchers.Main) {
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(imageView)
        delay(2000)
        findNavController().navigate(SplashDirections.actionSplashToHome2())
         }
}
}


}