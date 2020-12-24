package com.example.okaz.Ui.HomeFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.okaz.Repo.Repo

class HomeViewModel@ViewModelInject constructor(private val theAppRepoForAll: Repo): ViewModel() {
}