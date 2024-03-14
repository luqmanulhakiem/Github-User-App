package com.dicoding.githser.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githser.R
import com.dicoding.githser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val KEY_USERNAME = "key_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1, R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]

        val userName = intent.getStringExtra(KEY_USERNAME)
        val username: TextView = findViewById(R.id.tiUsername)
        val name: TextView = findViewById(R.id.tiName)

        userName?.let { detailViewModel.getDetailUser(it) }
        val tabs: TabLayout = findViewById(R.id.tabs)

        detailViewModel.detailUser.observe(this) { user ->
            username.text = user.login
            name.text = user.name
            Glide.with(this).load("${user.avatarUrl}}").into(binding.tiAvatar)
            tabs.getTabAt(0)?.text = resources.getString(TAB_TITLES[0], user.followers)
            tabs.getTabAt(1)?.text = resources.getString(TAB_TITLES[1], user.following)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, userName.toString())
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun showLoading(it: Boolean) {
        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
    }
}