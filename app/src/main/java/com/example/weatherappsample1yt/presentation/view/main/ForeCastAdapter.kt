package com.example.weatherappsample1yt.presentation.view.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.data.model.format.HourlyDetail
import com.example.weatherappsample1yt.databinding.ForecastViewholderBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ForeCastAdapter : RecyclerView.Adapter<ForeCastAdapter.ForeCastViewHolder>() {
    private var _temperatureUnitOptions: TemperatureUnitOptions? = null

    companion object {
        fun iconKey(codeKey: String?): String {
            return when (codeKey) {
                "01d", "01n" -> "sunny"
                "02d", "02n" -> "cloudy_sunny"
                "03d", "03n" -> "cloudy_sunny"
                "04d", "04n" -> "cloudy"
                "09d", "09n" -> "rainy"
                "10d", "10n" -> "rainy"
                "11d", "11n" -> "storm"
                "13d", "13n" -> "snow"
                "50d", "50n" -> "mist"
                else -> "sunny"
            }
        }

        val iconMap: Map<String, Int> = mapOf(
            "sunny" to R.drawable.sunny_icon,
            "cloudy_sunny" to R.drawable.cloudy_sunny,
            "cloudy" to R.drawable.cloudy,
            "rainy" to R.drawable.rainy,
            "storm" to R.drawable.storm,
            "snow" to R.drawable.snowy,
            "mist" to R.drawable.haze
        )
        val outputFormat = SimpleDateFormat("EEE", Locale.ENGLISH).apply {
            timeZone = TimeZone.getDefault()
        }
    }

    inner class ForeCastViewHolder(binding: ForecastViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val pic = binding.pic
        val titleText = binding.titleText
        val hourText = binding.hourText
        val tempText = binding.tempText
    }

    private fun parseDate(dateString: String): Date? {
        Log.d("ForeCastAdapter", "Parsing date string: $dateString")
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            format.parse(dateString)
        } catch (e: Exception) {
            Log.e("ForeCastAdapter", "Failed to parse date: $dateString", e)
            null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForecastViewholderBinding.inflate(inflater, parent, false)
        return ForeCastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForeCastViewHolder, position: Int) {
        val forecast = differ.currentList[position]
        val timeZone = TimeZone.getDefault()

        val date = forecast.time?.let { parseDate(it) }
        val calendar = Calendar.getInstance(timeZone).apply {
            time = date ?: Date()
        }

        val currentDateTime = Calendar.getInstance(timeZone)
        val dayOfWeek = date?.let { outputFormat.format(it) }

        if (calendar.get(Calendar.DAY_OF_YEAR) == currentDateTime.get(Calendar.DAY_OF_YEAR) && calendar.get(
                Calendar.YEAR
            ) == currentDateTime.get(Calendar.YEAR)
        ) {
            holder.titleText.setText(R.string.today)
        } else {
            holder.titleText.text = dayOfWeek
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val timeType = if (hour < 12) "AM" else "PM"
        val hour12 = calendar.get(Calendar.HOUR)

        val iconType: String? = forecast.icon
        val drawableResourceId = iconMap[iconKey(iconType)] ?: R.drawable.sunny_icon

        Glide.with(holder.itemView.context).load(drawableResourceId)
            .placeholder(R.drawable.sunny_icon).error(R.drawable.baseline_running_with_errors_24)
            .into(holder.pic)

        holder.hourText.text = holder.itemView.context.getString(
            R.string.temperatur_format, hour12, timeType
        )

        holder.tempText.text = _temperatureUnitOptions?.let {
            forecast.temp?.formatTemperature(
                it,
            )
        } ?: "N/A"
    }

    override fun getItemCount(): Int = differ.currentList.size

    private val differCallBack = object : DiffUtil.ItemCallback<HourlyDetail>() {
        override fun areItemsTheSame(oldItem: HourlyDetail, newItem: HourlyDetail): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: HourlyDetail, newItem: HourlyDetail): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    fun updateTemperatureUnitOptions(newUnitOptions: TemperatureUnitOptions?) {
        _temperatureUnitOptions = newUnitOptions
        notifyDataSetChanged()
    }
}

