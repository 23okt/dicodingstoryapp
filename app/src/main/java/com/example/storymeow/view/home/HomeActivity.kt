package com.example.storymeow.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storymeow.R
import com.example.storymeow.adapter.StoryAdapter
import com.example.storymeow.data.Result
import com.example.storymeow.databinding.ActivityHomeBinding
import com.example.storymeow.view.ViewModelFactory
import com.example.storymeow.view.camera.CameraActivity
import com.example.storymeow.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        binding.fabUpload.setOnClickListener {
            startActivity(Intent(this,CameraActivity::class.java))
        }

        viewModel.getSession().observe(this){
            if (!it.isLogin){
                val intent = Intent(this,WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }else{
                setupAction()
            }
        }
        val layoutManager = LinearLayoutManager(this)
        binding.homeRecyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.homeRecyclerView.addItemDecoration(itemDecoration)
    }

    private fun setupAction(){
        lifecycleScope.launch {
            viewModel.getStories().observe(this@HomeActivity) { story ->
                when (story) {
                    is Result.Error -> {
                        binding.progressHome.visibility = View.INVISIBLE
                        val error = story.error
                        Toast.makeText(this@HomeActivity, error, Toast.LENGTH_SHORT).show()
                    }

                    is Result.Loading -> {
                        binding.progressHome.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressHome.visibility = View.INVISIBLE
                        val adapter = StoryAdapter()
                        adapter.submitList(story.data)
                        binding.homeRecyclerView.adapter = adapter
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                lifecycleScope.launch {
                    viewModel.logout()
                    val intent = Intent(this@HomeActivity,WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.setting->{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}