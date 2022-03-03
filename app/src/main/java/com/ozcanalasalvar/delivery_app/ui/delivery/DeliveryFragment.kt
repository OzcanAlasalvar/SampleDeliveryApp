package com.ozcanalasalvar.delivery_app.ui.delivery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ozcanalasalvar.delivery_app.R
import com.ozcanalasalvar.delivery_app.databinding.FragmentDeliveryBinding
import com.ozcanalasalvar.delivery_app.utils.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DeliveryFragment : Fragment(R.layout.fragment_delivery) {

    lateinit var navController: NavController

    private val binding by viewBinding(FragmentDeliveryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_delivery_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()


        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
    }
}