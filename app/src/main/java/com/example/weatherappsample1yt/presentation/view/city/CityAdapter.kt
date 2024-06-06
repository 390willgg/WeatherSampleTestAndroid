package com.example.weatherappsample1yt.presentation.view.city

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappsample1yt.data.model.format.DataItemCity
import com.example.weatherappsample1yt.databinding.CityViewholderBinding
import com.example.weatherappsample1yt.presentation.view.main.MainActivity

class CityAdapter : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding =
            CityViewholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = differ.currentList[position]
        holder.bind(city)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class CityViewHolder(private val binding: CityViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(city: DataItemCity) {
            binding.cityText.text = city.cityName
            binding.cityText.setOnClickListener {
                navigateToMainActivity(city)
            }
        }

        private fun navigateToMainActivity(city: DataItemCity) {
            Toast.makeText(binding.root.context, "Chip clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(binding.root.context, MainActivity::class.java).apply {
                putExtra("lat", city.latitude)
                putExtra("lon", city.longitude)
                putExtra("name", city.cityName)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            binding.root.context.startActivity(intent)
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<DataItemCity>() {
        override fun areItemsTheSame(oldItem: DataItemCity, newItem: DataItemCity): Boolean {
            return oldItem.cityName == newItem.cityName // Assuming cityName is unique
        }

        override fun areContentsTheSame(oldItem: DataItemCity, newItem: DataItemCity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
}