package com.dicoding.githser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githser.data.response.ItemsItem
import com.dicoding.githser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvList.addItemDecoration(itemDecoration)

        mainViewModel.listUser.observe(this) { listUser ->
            setListUserData(listUser)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }


        /**
         * Search
         * */
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                mainViewModel.findUser(searchView.text.toString())
                false
            }
        }
    }

    private fun showLoading(it: Boolean) {
        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
    }

    private fun setListUserData(listUser: List<ItemsItem>) {
        val adapter = UserAdapter(this)
        adapter.submitList(listUser)
        binding.rvList.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        val intentToDetail = Intent(this, DetailActivity::class.java)
        val user = (binding.rvList.adapter as UserAdapter).currentList[position]
        intentToDetail.putExtra(DetailActivity.KEY_USERNAME, user.login)
        startActivity(intentToDetail)
    }
}