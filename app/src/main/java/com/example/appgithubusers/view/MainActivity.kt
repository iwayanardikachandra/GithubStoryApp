package com.example.appgithubusers.view

import UserAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubusers.R
import com.example.appgithubusers.databinding.ActivityMainBinding
import com.example.appgithubusers.model.ItemsItem
import com.example.appgithubusers.model.SettingPreferences
import com.example.appgithubusers.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val viewModel by viewModels<MainViewModel>{
        MainViewModel.MainFactory(SettingPreferences(context = this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        observeViewModel()
        observeThemeSettings()
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter().apply {
            setOnUserItemClickListener(object : UserAdapter.OnUserItemClickListener {
                override fun onUserItemClick(user: ItemsItem) {
                    navigateToDetail(user)
                }
            })
        }

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
        }

        with(binding) {
            search.setOnClickListener { searchUser() }
            inputsearch.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loadingLive.observe(this) { showProgressBar(it) }

        viewModel.getSearchUser().observe(this) {
            if (it != null) {
                adapter.setItems(it as ArrayList<ItemsItem>)
                showProgressBar(false)
            }
        }
    }

    private fun observeThemeSettings() {
        viewModel.getThemeSetting().observe(this@MainActivity) {
            AppCompatDelegate.setDefaultNightMode(
                if (it) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun navigateToDetail(data: ItemsItem) {
        Intent(this@MainActivity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_ID, data.id)
            putExtra(DetailActivity.EXTRA_URL, data.avatarUrl)
            putExtra(DetailActivity.EXTRA_USERNAME, data.login)
            startActivity(this)
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun searchUser() {
        val query = binding.inputsearch.text.toString()
        if (query.isEmpty()) return
        showProgressBar(true)
        viewModel.searchUser(query)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        inflateMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
                true
            }
            R.id.action_setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun inflateMenu(menu: Menu) {
        menuInflater.inflate(R.menu.menu, menu)
    }
}