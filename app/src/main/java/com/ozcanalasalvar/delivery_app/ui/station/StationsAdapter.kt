package com.ozcanalasalvar.delivery_app.ui.station

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ozcanalasalvar.delivery_app.R
import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.databinding.StationItemCellBinding

class StationsAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Station, StationsAdapter.StationsViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationsViewHolder {
        val binding =
            StationItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StationsViewHolder(private val binding: StationItemCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                binding.imgFav.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val station = getItem(position)
                        listener.onFavoriteClicked(station)
                    }

                }

                binding.tvTravel.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val station = getItem(position)
                        listener.onTravelClicked(station)
                    }
                }
            }
        }

        fun bind(station: Station) {
            binding.apply {
                tvStationName.text = station.name
                val stockText = "" + station.stock + " / " + station.capacity
                tvStock.text = stockText
                imgFav.setImageResource(if (station.isFavorite) R.drawable.start_active else R.drawable.star_inactive)

                binding.tvTravel.visibility =
                    if (station.need == 0) View.INVISIBLE else View.VISIBLE
            }
        }
    }

    interface OnItemClickListener {
        fun onTravelClicked(station: Station)
        fun onFavoriteClicked(station: Station)
    }

    class DiffCallback : DiffUtil.ItemCallback<Station>() {
        override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean =
            oldItem === newItem

    }


}