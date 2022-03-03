package com.ozcanalasalvar.delivery_app.ui.favortie


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ozcanalasalvar.delivery_app.R
import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.databinding.FragmentFavoriteStationsBinding
import com.ozcanalasalvar.delivery_app.utils.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoriteStationsFragment : Fragment(R.layout.fragment_favorite_stations),
    FavoriteStationsAdapter.OnItemClickListener {

    private val viewModel: FavoriteViewModel by viewModels()

    private val binding by viewBinding(FragmentFavoriteStationsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterFavorite = FavoriteStationsAdapter(this)

        binding.apply {
            recyclerViewFavorite.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = adapterFavorite
            }
        }

        viewModel.stations.observe(viewLifecycleOwner, Observer {
            adapterFavorite.submitList(it)
        })
    }

    override fun onFavoriteClicked(station: Station) {
        viewModel.onFavoriteStatusChanged(station)
    }
}