package com.example.appgithubusers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.appgithubusers.R
import com.example.appgithubusers.databinding.ActivitySettingsBinding
import com.example.appgithubusers.model.SettingPreferences
import com.example.appgithubusers.viewmodels.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsBinding: ActivitySettingsBinding
    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.SettingsFactory(SettingPreferences(context = this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settingsBinding.root)

        settingsBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        //-------------------------------------------------------------------------------------//

        viewModel.getThemeSettings().observe(this@SettingsActivity) { isDarkModeActive ->
            val themeTextRes = if (isDarkModeActive) R.string.darkMode else R.string.lightMode
            settingsBinding.switchTheme.text = getString(themeTextRes)
            AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            settingsBinding.switchTheme.isChecked = isDarkModeActive
            showProgressBar(false)
        }

        settingsBinding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSettings(isChecked)
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        settingsBinding.settingprogressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}