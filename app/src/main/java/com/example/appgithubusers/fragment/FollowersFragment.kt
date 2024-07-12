package com.example.appgithubusers.fragment

import UserAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubusers.R
import com.example.appgithubusers.databinding.FragmentFollowersBinding
import com.example.appgithubusers.model.ItemsItem
import com.example.appgithubusers.view.DetailActivity
import com.example.appgithubusers.viewmodels.FollowersViewModel

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowersBinding.bind(view)
        initializeViews()
        observeFollowers()
        fetchFollowers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeViews() {
        val args = requireArguments()
        username = args.getString(DetailActivity.EXTRA_USERNAME).toString()

        adapter = UserAdapter()
        binding.rvFollow.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = this@FollowersFragment.adapter
        }
    }

    private fun observeFollowers() {
        viewModel = ViewModelProvider(this).get(FollowersViewModel::class.java)
        viewModel.getListFollower().observe(viewLifecycleOwner) { followers ->
            followers?.let {
                adapter.setItems(it as ArrayList<ItemsItem>)
                showProgressBar(false)
            }
        }
    }

    private fun fetchFollowers() {
        showProgressBar(true)
        viewModel.fetchFollowers(username)
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}