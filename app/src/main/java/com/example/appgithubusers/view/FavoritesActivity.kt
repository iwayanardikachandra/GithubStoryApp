package com.example.appgithubusers.view

import UserAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubusers.databinding.ActivityFavoritesBinding
import com.example.appgithubusers.model.ItemsItem
import com.example.appgithubusers.model.UserFavorite
import com.example.appgithubusers.viewmodels.FavoritesViewModel

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoritesBinding: ActivityFavoritesBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoritesBinding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(favoritesBinding.root)

        setupToolbar()
        setupRecyclerView()
        initializeViewModel()
        observeFavoriteUsers()
    }

    private fun setupToolbar() {
        favoritesBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter().apply {
            setOnUserItemClickListener(object : UserAdapter.OnUserItemClickListener {
                override fun onUserItemClick(user: ItemsItem) {
                    navigateToUserDetail(user)
                }
            })
        }

        favoritesBinding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = this@FavoritesActivity.adapter
        }
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]
    }

    private fun observeFavoriteUsers() {
        viewModel.getFavoriteUsers()?.observe(this) { users ->
            users?.let {
                val list = convertToItemsItemList(users)
                adapter.setItems(list)
            }
            showProgressBar(false)
        }
        showProgressBar(true)
    }

    private fun convertToItemsItemList(users: List<UserFavorite>): List<ItemsItem> {
        return users.map { user ->
            ItemsItem(
                login = user.login,
                avatarUrl = user.avatarURL,
                id = user.id
            )
        }
    }

    private fun navigateToUserDetail(user: ItemsItem) {
        Intent(this@FavoritesActivity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            putExtra(DetailActivity.EXTRA_ID, user.id)
            putExtra(DetailActivity.EXTRA_URL, user.avatarUrl)
            startActivity(this)
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        favoritesBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
