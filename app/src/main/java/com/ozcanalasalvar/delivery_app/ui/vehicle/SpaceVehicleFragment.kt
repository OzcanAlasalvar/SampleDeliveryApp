package com.ozcanalasalvar.delivery_app.ui.vehicle

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ozcanalasalvar.delivery_app.R
import com.ozcanalasalvar.delivery_app.databinding.FragmentSpaceVehicleBinding
import com.ozcanalasalvar.delivery_app.ui.base.BaseFragment
import com.ozcanalasalvar.delivery_app.utils.ext.OnProgressChanged
import com.ozcanalasalvar.delivery_app.utils.ext.showFailurePopup
import com.ozcanalasalvar.delivery_app.utils.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SpaceVehicleFragment : BaseFragment(R.layout.fragment_space_vehicle) {

    private val binding by viewBinding(FragmentSpaceVehicleBinding::bind)

    private val viewModel: VehicleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            pnlGoOn.setOnClickListener {
                viewModel.createVehicle()
            }

            seekBarCapacity.OnProgressChanged {
                viewModel.vehicleCapacity = it
            }

            seekBarDurability.OnProgressChanged {
                viewModel.vehicleDurability = it
            }

            seekBarVelocity.OnProgressChanged {
                viewModel.vehicleVelocity = it
            }

            edtVehicleName.addTextChangedListener {
                viewModel.vehicleName = it.toString()
            }
        }

        viewModel.totalPoint.observe(viewLifecycleOwner, Observer {
            binding.tvTotalPoint.text = it.toString()
        })


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.createVehicleEvent.collect {
                when (it) {
                    is VehicleViewModel.VehicleEvent.ShowInvalidInputMessage -> {
                        requireView().showFailurePopup(it.msg)
                    }
                    is VehicleViewModel.VehicleEvent.NavigateToDelivery -> {
                        findNavController().navigate(SpaceVehicleFragmentDirections.actionSpaceVehicleFragmentToSpaceDeliveryFragment())
                    }
                }
            }
        }
    }
}