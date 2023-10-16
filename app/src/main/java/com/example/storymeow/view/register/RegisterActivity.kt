package com.example.storymeow.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storymeow.data.Result
import com.example.storymeow.databinding.ActivityRegisterBinding
import com.example.storymeow.view.ViewModelFactory
import com.example.storymeow.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerAnimation()
        setupAction()

        supportActionBar?.hide()
    }
    private fun registerAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30F, 30F).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.registerTitle, View.ALPHA, 1F).setDuration(500)
        val textName = ObjectAnimator.ofFloat(binding.nameRegister, View.ALPHA, 1F).setDuration(500)
        val editName = ObjectAnimator.ofFloat(binding.nameLayout, View.ALPHA, 1F).setDuration(500)
        val textEmail =
            ObjectAnimator.ofFloat(binding.emailRegister, View.ALPHA, 1F).setDuration(500)
        val editEmail = ObjectAnimator.ofFloat(binding.emailLayout, View.ALPHA, 1F).setDuration(500)
        val textPassword =
            ObjectAnimator.ofFloat(binding.passwordRegister, View.ALPHA, 1F).setDuration(500)
        val editPassword =
            ObjectAnimator.ofFloat(binding.passwordLayout, View.ALPHA, 1F).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1F).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                textName,
                editName,
                textEmail,
                editEmail,
                textPassword,
                editPassword,
                button
            )
            start()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun setupAction(){
        binding.buttonRegister.setOnClickListener {
            val name = binding.nameEdit.text.toString()
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

                viewModel.register(name,email,password).observe(this){ user->
                    when (user) {
                        is Result.Error -> {
                            binding.progressRegister.visibility = View.INVISIBLE
                            val error = user.error
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }

                        is Result.Loading -> {
                            binding.progressRegister.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressRegister.visibility = View.INVISIBLE
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeay")
                                setMessage("Anda berhasil mendaftar. Sudah siap menjelajah?")
                                setPositiveButton("Lanjut") { _, _ ->
                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                        }
                    }
                }
            }
        }
    }
}