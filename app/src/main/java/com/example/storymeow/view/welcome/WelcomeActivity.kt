package com.example.storymeow.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.storymeow.databinding.ActivityWelcomeBinding
import com.example.storymeow.view.login.LoginActivity
import com.example.storymeow.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
        setupAction()

        supportActionBar?.hide()
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30F, 30F).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA,1F).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA,1F).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1F).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1F).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login,signup)
        }

        AnimatorSet().apply {
            playSequentially(title,desc,together)
            start()
        }
    }
    private fun setupAction(){
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.signupButton.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

}