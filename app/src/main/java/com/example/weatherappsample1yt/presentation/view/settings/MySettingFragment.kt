package com.example.weatherappsample1yt.presentation.view.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySettingFragment : PreferenceFragmentCompat() {
    private val settingViewModel: SettingViewModel by viewModels()
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        val dropDownPreferenceApiProviders: DropDownPreference? = findPreference("api_providers")
        dropDownPreferenceApiProviders?.setOnPreferenceChangeListener { _, newValue ->
            val newApiProvider = ApiProviderOptions.valueOf(newValue.toString())
            settingViewModel.updateApiProvider(newApiProvider)
            settingViewModel.loadApiProvider()
            true
        }

        val dropDownPreferenceUnitTemperature: DropDownPreference? = findPreference("unit_temperatures")
        dropDownPreferenceUnitTemperature?.setOnPreferenceChangeListener { _, newValue ->
            val newUniTemperature = TemperatureUnitOptions.valueOf(newValue.toString())
            settingViewModel.updateTemperatureUnit(newUniTemperature)
            settingViewModel.loadTemperatureUnit()
            true
        }
    }
}