package com.ozcanalasalvar.delivery_app.ui.favortie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ozcanalasalvar.delivery_app.R
import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.databinding.FavoriteItemCellBinding

class FavoriteStationsAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Station, FavoriteStationsAdapter.FavoriteViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            FavoriteItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class FavoriteViewHolder(private val binding: FavoriteItemCellBinding) :
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
            }
        }

        fun bind(station: Station) {
            binding.apply {
                tvStationName.text = station.name
                val stockText = "" + station.stock + " / " + station.capacity
                tvStock.text = stockText
                imgFav.setImageResource(if (station.isFavorite) R.drawable.start_active else R.drawable.star_inactive)
            }
        }
    }

    interface OnItemClickListener {
        fun onFavoriteClicked(station: Station)
    }

    class DiffCallback : DiffUtil.ItemCallback<Station>() {
        override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean =
            oldItem === newItem

    }


}