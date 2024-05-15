package com.example.weatherappsample1yt.data.model.format

import com.example.weatherappsample1yt.presentation.view.main.TemperatureUnitOptions

data class ForecastWeatherData(
    val country : String?,
    val city : String?,
    val lat : Double?,
    val lon : Double?,
    val timezone : String?, // Added timezone field
    val forecasts : ForecastDetail?,
) {
    fun convertTemperature(unitTemp : TemperatureUnitOptions) : ForecastWeatherData {
        val dailyDetails = forecasts?.dailyDetails?.map { it.convertTemperature(unitTemp) }
        val hourlyDetails = forecasts?.hourlyDetails?.map { it.convertTemperature(unitTemp) }
        return this.copy(
            forecasts = ForecastDetail(dailyDetails, hourlyDetails)
        )
    }
}

data class ForecastDetail(
    val dailyDetails: List<DailyDetail>?,
    val hourlyDetails: List<HourlyDetail>?
)

data class DailyDetail(
    val date : String?,
    val temp : Double?,
    val maxTemp : Double?,
    val minTemp : Double?,
    val condition : String?,
    val icon : String?,
    val description : String?,
    val precipitation : Double?,
    val pressure : Double?,
    val humidity : Int?,
    val windSpeed : Double?,
    val windDirection : String?,
    val uvIndex : Double?, // Added uvIndex field
) {
    fun convertTemperature(unitTemp : TemperatureUnitOptions) : DailyDetail {
        val temp =
            if (unitTemp == TemperatureUnitOptions.Celsius) temp else temp?.times(9 / 5)?.minus(32)
        val maxTemp =
            if (unitTemp == TemperatureUnitOptions.Celsius) maxTemp else maxTemp?.times(9 / 5)
                ?.minus(32)
        val minTemp =
            if (unitTemp == TemperatureUnitOptions.Celsius) minTemp else minTemp?.times(9 / 5)
                ?.minus(32)
        return this.copy(
            temp = temp, maxTemp = maxTemp, minTemp = minTemp
        )
    }
}

data class HourlyDetail(
    val time : String?,
    val temp : Double?,
    val maxTemp : Double?,
    val minTemp : Double?,
    val feelsLike : Double?,
    val condition : String?,
    val icon : String?,
    val pressure : Double?,
    val humidity : Int?,
    val windSpeed : Double?,
    val windDirection : String?,
    val description : String?,
    val precipitation : Double?,
    val uvIndex : Double?, // Added uvIndex field
) {
    fun convertTemperature(unitTemp : TemperatureUnitOptions) : HourlyDetail {
        val temp =
            if (unitTemp == TemperatureUnitOptions.Celsius) temp else temp?.times(9 / 5)?.minus(32)
        val maxTemp =
            if (unitTemp == TemperatureUnitOptions.Celsius) maxTemp else maxTemp?.times(9 / 5)
                ?.minus(32)
        val minTemp =
            if (unitTemp == TemperatureUnitOptions.Celsius) minTemp else minTemp?.times(9 / 5)
                ?.minus(32)
        val feelsLike =
            if (unitTemp == TemperatureUnitOptions.Celsius) feelsLike else feelsLike?.times(9 / 5)
                ?.minus(32)
        return this.copy(
            temp = temp, maxTemp = maxTemp, minTemp = minTemp, feelsLike = feelsLike
        )
    }
}