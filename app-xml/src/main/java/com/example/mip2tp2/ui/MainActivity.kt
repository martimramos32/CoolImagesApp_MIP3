package com.example.mip2tp2.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mip2tp2.R
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.mip2tp2.data.repository.FavoritesManager
import com.example.mip2tp2.databinding.ActivityMainBinding
import com.example.mip2tp2.ui.adapter.ImageAdapter
import com.example.mip2tp2.ui.viewmodel.MainViewModel

/**
 * MainActivity - The single entry point of the application.
 * Connects the Unsplash UI Layouts, the ImageAdapter, and the MainViewModel logic.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Evaluate Dark Theme Preference Initial State mapping securely avoiding flickers
        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDark = prefs.getBoolean("is_dark", false)
        val targetMode = if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        if (AppCompatDelegate.getDefaultNightMode() != targetMode) {
            AppCompatDelegate.setDefaultNightMode(targetMode)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        favoritesManager = FavoritesManager(this)
        
        imageAdapter = ImageAdapter(
            onFavoriteClick = { image ->
                val result = favoritesManager.toggleFavorite(image)
                if (!result.first && !result.second) {
                   // Tried to add more than 5
                   Toast.makeText(this, "Cannot add more than 5 favorites.", Toast.LENGTH_SHORT).show()
                }
            },
            favoritesManager = favoritesManager
        )

        // Set Toolbar as ActionBar
        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = imageAdapter
        }
        
        // Setup Infinite Scroll Listener Pagination Ruleset (Step 16)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) { // Sliding Downards strictly
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                    // Predictively hit pre-fetch bounds intelligently sliding threshold mapping
                    if ((visibleItemCount + pastVisibleItems) >= (totalItemCount - 5)) {
                        viewModel.fetchRandomImages(isInitial = false)
                    }
                }
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchRandomImages(isInitial = true)
        }
    }

    private fun observeViewModel() {
        // Observe the images list to submit bounds to the adapter
        viewModel.images.observe(this) { imagesList ->
            if (imagesList != null) {
                imageAdapter.submitList(imagesList)
            }
        }

        // Observe the loading state to toggle visual indicators gracefully
        viewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading

            if (isLoading && imageAdapter.itemCount == 0) {
                binding.progressBar.visibility = android.view.View.VISIBLE
            } else {
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
        
        // Explicitly map independent progression bar indicators
        viewModel.isPaginating.observe(this) { isPaginating ->
             binding.paginationProgressBar.visibility = if (isPaginating) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh local items states just in case
        imageAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorites -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
                true
            }
            R.id.action_theme_toggle -> {
                toggleTheme()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleTheme() {
        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isCurrentlyDark = prefs.getBoolean("is_dark", false)
        
        val newMode = if (isCurrentlyDark) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(newMode)
        
        prefs.edit().putBoolean("is_dark", !isCurrentlyDark).apply()
    }
}
