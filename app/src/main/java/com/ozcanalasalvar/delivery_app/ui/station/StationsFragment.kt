package com.ozcanalasalvar.delivery_app.ui.station

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ozcanalasalvar.delivery_app.R
import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.databinding.FragmentStationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.recyclerview.widget.PagerSnapHelper

import androidx.recyclerview.widget.SnapHelper
import com.ozcanalasalvar.delivery_app.ui.MainViewModel
import com.ozcanalasalvar.delivery_app.utils.ext.*
import kotlinx.coroutines.flow.collect


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class StationsFragment : Fragment(R.layout.fragment_stations), StationsAdapter.OnItemClickListener {

    private val viewModel: StationsViewModel by viewModels()

    private val sharedViewModel: MainViewModel by activityViewModels()

    private val binding by viewBinding(FragmentStationsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stationsAdapter = StationsAdapter(this)

        binding.apply {

            recyclerViewStations.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = stationsAdapter

                val snapHelper: SnapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(this)
            }


            searchView.onQueryTextChanged {
                viewModel.searchQuery.value = it
            }

            pnlNext.setOnClickListener {
                recyclerViewStations.scrollToNext()
            }

            pnlPrevious.setOnClickListener {
                recyclerViewStations.scrollToPrevious()
            }
        }


        viewModel.stations.observe(viewLifecycleOwner, { stationsAdapter.submitList(it) })


        viewModel.vehicleLiveData.observe(viewLifecycleOwner, { vehicle ->
            vehicle.station?.let { station ->
                binding.pnlCurrentStation.tvStationName.text = station.name
                binding.pnlCurrentStation.tvStationName.text = station.name
                val stockText = "" + station.stock + " / " + station.capacity
                binding.pnlCurrentStation.tvStock.text = stockText
                binding.pnlCurrentStation.imgFav.setImageResource(if (station.isFavorite) R.drawable.start_active else R.drawable.star_inactive)
                binding.pnlCurrentStation.imgFav.setOnClickListener {
                    viewModel.onFavoriteStatusChanged(station)
                }
            }
            vehicle.vehicle.let {
                binding.tvVehicleName.text = it.name
                binding.tvDamageCapacity.text = it.durabilityValue.toString()
            }

        })

        viewModel.prefStatusLiveData.observe(viewLifecycleOwner, {
            binding.tvUgs.text = it.UGS.toString()
            binding.tvEus.text = it.EUS.toString()
            binding.tvDs.text = it.DS.toString()
        })

        sharedViewModel.hasDamage.observe(viewLifecycleOwner, {
            viewModel.sendDamageToVehicle()
        })


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.stationEvent.collect {
                when (it) {
                    StationsViewModel.StationEvent.HideLoading -> {
                        binding.progressBar.gone()
                    }
                    StationsViewModel.StationEvent.ShowLoading -> {
                        binding.progressBar.visible()
                    }
                    is StationsViewModel.StationEvent.ShowNetworkError -> {
                        binding.progressBar.gone()
                        requireView().showNetworkError(requireContext().getString(it.msg)) {
                            viewModel.onReloadClick()
                        }
                    }
                    is StationsViewModel.StationEvent.ShowActionFailureMessage -> {
                        requireView().showFailurePopup(requireContext().getString(it.msg))
                    }
                    is StationsViewModel.StationEvent.ShowActionSuccessMessage -> {
                        requireView().showSuccessPopup(requireContext().getString(it.msg))
                    }
                    is StationsViewModel.StationEvent.ShowDeliveryFinishedDialog -> {
                        val dialog = AlertDialog.Builder(requireContext()).apply {
                            setTitle(R.string.delivery_over)
                            setMessage(R.string.return_home)
                            setPositiveButton(R.string.ok) { dialog, _ ->
                                sharedViewModel.stopTimer()
                                viewModel.resetDelivery()
                                dialog.dismiss()
                            }
                        }.create()
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.show()

                    }
                }
            }

        }

    }


    override fun onTravelClicked(station: Station) {
        viewModel.onStartDeliveryToStation(station)
    }

    override fun onFavoriteClicked(station: Station) {
        viewModel.onFavoriteStatusChanged(station)
    }


}