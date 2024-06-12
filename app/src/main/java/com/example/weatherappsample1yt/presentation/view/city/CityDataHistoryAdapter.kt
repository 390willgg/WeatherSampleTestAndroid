package com.example.weatherappsample1yt.presentation.view.city

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappsample1yt.data.model.format.CityData
import com.example.weatherappsample1yt.databinding.CityItemHistoryDataBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityDataHistoryAdapter @Inject constructor(private val onDelete: (position: Int) -> Unit) :
    RecyclerView.Adapter<CityDataHistoryAdapter.CityDataHistoryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CityDataHistoryAdapter.CityDataHistoryViewHolder {
        val binding =
            CityItemHistoryDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityDataHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CityDataHistoryAdapter.CityDataHistoryViewHolder,
        position: Int
    ) {
        val city = differ.currentList[position]
        holder.bind(city, position)
    }

    override fun getItemCount(): Int {
        Log.i("CityDataHistoryAdapter", "getItemCount: ${differ.currentList.size}")
        return differ.currentList.size
    }

    inner class CityDataHistoryViewHolder(private val binding: CityItemHistoryDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cityData: CityData, position: Int) {
            binding.cityName.text = cityData.cityName
            binding.deleteButtonItem.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    onDelete(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<CityData>() {
        override fun areItemsTheSame(oldItem: CityData, newItem: CityData): Boolean {
            return oldItem.cityName == newItem.cityName // Assuming cityName is unique
        }

        override fun areContentsTheSame(oldItem: CityData, newItem: CityData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
}