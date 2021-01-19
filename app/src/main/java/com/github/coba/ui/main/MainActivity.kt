package com.github.coba.ui.main

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.coba.R
import com.github.coba.data.model.User
import com.github.coba.databinding.ActivityMainBinding
import com.github.coba.ui.detail.DetailUserActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var welcome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLang()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.app_name)

        welcome = findViewById(R.id.tv_welcome)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    startActivity(it)
                }
            }
        })
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
            rvUser.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayout.VERTICAL))
        }

        viewModel.getSearchUsers().observe(this, {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchUser(query)
                welcome.visibility = View.GONE
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        val lang = menu?.findItem(R.id.language)
        lang.setOnMenuItemClickListener {
            showChangeLang()
            return@setOnMenuItemClickListener true
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun showChangeLang() {
        val listLang = arrayOf("English", "Bahasa Indonesia")
        AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Dialog_Alert)
            .setTitle(R.string.choose)
            .setSingleChoiceItems(listLang, -1) { dialog, which ->
                if (which == 0) {
                    setLang("en")
                    recreate()
                } else if (which == 1) {
                    setLang("id")
                    recreate()
                }

                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun setLang(lang: String) {
        val locale: Locale = Locale(lang)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_lang", lang)
        editor.apply()
    }

    private fun getLang() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_lang", "")
        setLang(language.toString())
    }

    private fun searchUser(q: String){
        binding.apply {
            if (q.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUsers(q)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}