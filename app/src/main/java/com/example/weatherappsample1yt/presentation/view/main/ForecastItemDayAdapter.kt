package com.example.weatherappsample1yt.presentation.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.data.model.format.DailyDetail
import com.example.weatherappsample1yt.data.model.format.TemperatureModel.Companion.formatTemperatureRange
import com.example.weatherappsample1yt.databinding.ForecastViewholderDayitemBinding
import com.example.weatherappsample1yt.presentation.view.main.ForeCastAdapter.Companion.iconKey
import com.example.weatherappsample1yt.presentation.view.main.ForeCastAdapter.Companion.iconMap
import java.text.SimpleDateFormat
import java.util.Locale

class ForecastItemDayAdapter :
    RecyclerView.Adapter<ForecastItemDayAdapter.ForecastItemDayViewHolder>() {
    private var _temperatureUnitOptions: TemperatureUnitOptions? = null

    inner class ForecastItemDayViewHolder(binding: ForecastViewholderDayitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val dayText = binding.dayText
        val picWeatherDay = binding.picWeatherDay
        val tempWeatherDayItem = binding.tempWeatherDayItem
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ForecastItemDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForecastViewholderDayitemBinding.inflate(
            inflater, parent, false
        )
        return ForecastItemDayViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ForecastItemDayViewHolder, position: Int
    ) {
        val forecast = differ.currentList[position]
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
        val date = forecast.date?.let { inputFormat.parse(it) }
        val dayOfWeek = date?.let { outputFormat.format(it) }
        val drawableResourceId = iconMap[iconKey(forecast.icon.toString())] ?: R.drawable.sunny_icon

        holder.dayText.text = dayOfWeek
        Glide.with(holder.itemView.context).load(drawableResourceId)
            .placeholder(R.drawable.sunny_day_icon)
            .error(R.drawable.baseline_running_with_errors_24).into(holder.picWeatherDay)

        holder.tempWeatherDayItem.text = formatTemperatureRange(
            holder.itemView.context,
            _temperatureUnitOptions,
            forecast.minTemp?.valueTemperature,
            forecast.maxTemp?.valueTemperature
                )
            }


    override fun getItemCount(): Int = differ.currentList.size

    private val differCallBack = object : DiffUtil.ItemCallback<DailyDetail>() {
        override fun areItemsTheSame(
            oldItem: DailyDetail, newItem: DailyDetail,
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: DailyDetail, newItem: DailyDetail,
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    fun updateTemperatureUnitOptions(newUnitOptions: TemperatureUnitOptions?) {
        _temperatureUnitOptions = newUnitOptions
        notifyDataSetChanged()
    }
}