package com.example.appgithubusers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.appgithubusers.adapter.ViewPagerAdapter
import com.example.appgithubusers.databinding.ActivityDetailBinding
import com.example.appgithubusers.viewmodels.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var pagerAdapterCombine: ViewPagerAdapter
    private val bundle = Bundle()
    private var isChecked = false

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        setupActionBar()
        initializeViewModel()
        setupViewPager()

        observeUserData()
        checkFavoriteStatus()
        setupFavoriteButtonListener()
    }

    private fun setupActionBar() {
        setSupportActionBar(detailBinding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        detailBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        showProgressBar(true)
    }

    private fun observeUserData() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        viewModel.detailUser(username.toString())

        bundle.putString(EXTRA_USERNAME, username)
        viewModel.user.observe(this) { user ->
            user?.let {
                with(detailBinding) {
                    name.text = user.name
                    login.text = user.login
                    followersNumber.text = "${user.followers}"
                    followingNumber.text = "${user.following}"
                    Glide.with(this@DetailActivity)
                        .load(user.avatarUrl)
                        .into(image)
                    showProgressBar(false)
                }
            }
        }
    }

    private fun checkFavoriteStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkForFavorite(intent.getIntExtra(EXTRA_ID, 0))
            withContext(Dispatchers.Main) {
                isChecked = count ?: 0 > 0
                detailBinding.btnFav.isChecked = isChecked
            }
        }
    }

    private fun setupFavoriteButtonListener() {
        detailBinding.btnFav.setOnClickListener {
            isChecked = !isChecked
            val username = intent.getStringExtra(EXTRA_USERNAME)
            val intentID = intent.getIntExtra(EXTRA_ID, 0)
            val avatarURL = intent.getStringExtra(EXTRA_URL)

            if (isChecked) {
                username?.let {
                    viewModel.addToFavorite(
                        username = it,
                        id = intentID,
                        avatarURL = avatarURL ?: ""
                    )
                }
            } else {
                viewModel.removeFromFavorite(intentID)
            }
            detailBinding.btnFav.isClickable
        }
    }

    private fun setupViewPager() {
        pagerAdapterCombine = ViewPagerAdapter(this, supportFragmentManager, bundle)
        detailBinding.viewpager.adapter = pagerAdapterCombine
        detailBinding.tab.setupWithViewPager(detailBinding.viewpager)
    }

    private fun showProgressBar(isLoading: Boolean) {
        detailBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
