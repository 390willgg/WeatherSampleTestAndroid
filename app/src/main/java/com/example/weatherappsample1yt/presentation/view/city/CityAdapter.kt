package com.example.weatherappsample1yt.presentation.view.city

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappsample1yt.data.model.format.CityData
import com.example.weatherappsample1yt.data.model.format.DataItemCity
import com.example.weatherappsample1yt.databinding.CityViewholderBinding
import com.example.weatherappsample1yt.presentation.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityAdapter @Inject constructor(private val saveData: (cityData: CityData) -> Unit) :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {
    private val scope = CoroutineScope(Dispatchers.Main)

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
            scope.launch {
                try {
                    saveData(
                        CityData(
                            city.latitude ?: 0.0,
                            city.longitude ?: 0.0,
                            city.cityName ?: "Unknown City"
                        )
                    )
                    withContext(Dispatchers.Main) {
                        val intent = Intent(binding.root.context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        binding.root.context.startActivity(intent)
                    }
                } catch (e: Exception) {
                    Log.e("CityAdapter", "Error saving city data", e)
                    e.printStackTrace()
                }
            }
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