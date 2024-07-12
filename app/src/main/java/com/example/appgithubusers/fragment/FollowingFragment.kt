package com.example.appgithubusers.fragment

import UserAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubusers.R
import com.example.appgithubusers.databinding.FragmentFollowingBinding
import com.example.appgithubusers.model.ItemsItem
import com.example.appgithubusers.view.DetailActivity
import com.example.appgithubusers.viewmodels.FollowingViewModel

class FollowingFragment : Fragment(R.layout.fragment_following) {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowingBinding.bind(view)

        val args = arguments
        username = args?.getString(DetailActivity.EXTRA_USERNAME).toString()

        adapter = UserAdapter()

        binding.rvFollow.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = this@FollowingFragment.adapter
        }

        viewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)
            viewModel.getListFollowing().observe(viewLifecycleOwner) { list ->
                adapter.setItems(list as ArrayList<ItemsItem>)
                showProgressBar(false)
            }
        viewModel.fetchFollowing(username)
        showProgressBar(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}