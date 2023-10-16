package com.example.storymeow.view.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.storymeow.databinding.ActivitySplashBinding
import com.example.storymeow.view.home.HomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            if (isInternetAvailable()) {
                startActivity(Intent(this, HomeActivity::class.java))
            }else{
                showNoInternetAlert()
            }
        }
        supportActionBar?.hide()
    }
    private fun isInternetAvailable(): Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showNoInternetAlert(){
        val alert = AlertDialog.Builder(this)
            alert.setTitle("Yah internet kamu mati")
            alert.setMessage("Silahkan nyalakan internet kamu terlebih dahulu yaa")
            alert.setPositiveButton("OK"){it,_->
                it.dismiss()
            }
        val dialog = alert.create()
        dialog.show()
    }
}