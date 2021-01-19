package com.github.coba.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.coba.R
import com.github.coba.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailUserViewModel::class.java)
        viewModel.setUserDetail(username.toString())
        viewModel.getUserDetail().observe(this, {
            if (it != null) {
                binding.apply {
                    tvUsername.text = it.login
                    if (it.login != null) {
                        tvFname.text = it.name
                    } else {
                        tvFname.text = it.login
                    }
                    tvDesc.text = it.html_url
                    tvFollowers.text = "${it.followers} ${getString(R.string.tab_1)}"
                    tvFollowing.text = "${it.following} ${getString(R.string.tab_2)}"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .circleCrop()
                        .into(ivProfile)
                }
            }
        })

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
    }
}