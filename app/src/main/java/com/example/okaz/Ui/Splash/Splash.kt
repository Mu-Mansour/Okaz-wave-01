package com.example.okaz.Ui.Splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.okaz.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.splash_fragment.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint
class Splash : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val theView=inflater.inflate(R.layout.splash_fragment,container,false)


        return theView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            YoYo.with(Techniques.FadeIn).duration(2000).playOn(imageView)
            delay(2000)
            findNavController().navigate(SplashDirections.actionSplashToHome2())
        }
    }


}