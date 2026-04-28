package com.example.mip2tp2.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mip2tp2.databinding.ActivityFavoritesBinding
import com.example.mip2tp2.ui.adapter.ImageAdapter
import com.example.mip2tp2.core.data.repository.FavoritesManager

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar and back navigation manually
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        favoritesManager = FavoritesManager(this)

        imageAdapter = ImageAdapter(
            onFavoriteClick = { _ ->
                // Because clicking a favorite inside the Favorites screen toggles it (removes it),
                // we should instantly refresh the dataset directly displaying the visual consequence
                loadFavoritesLocally()
            },
            favoritesManager = favoritesManager
        )

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = imageAdapter
        }

        loadFavoritesLocally()
    }

    override fun onResume() {
        super.onResume()
        // Always refresh the local disk storage UI representation upon reopening 
        // to handle case where users modified states from details screen
        loadFavoritesLocally()
    }

    private fun loadFavoritesLocally() {
        val favs = favoritesManager.getFavorites()
        imageAdapter.submitList(favs)
        
        if (favs.isEmpty()) {
            binding.emptyStateTextView.visibility = View.VISIBLE
        } else {
            binding.emptyStateTextView.visibility = View.GONE
        }
    }
}
