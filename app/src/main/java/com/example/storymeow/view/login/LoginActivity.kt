package com.example.storymeow.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.storymeow.data.Result
import com.example.storymeow.data.userpref.UserModel
import com.example.storymeow.databinding.ActivityLoginBinding
import com.example.storymeow.view.ViewModelFactory
import com.example.storymeow.view.home.HomeActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginAnimation()
        setupAction()

        supportActionBar?.hide()

    }
    private fun loginAnimation(){
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30F,30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.loginTitle, View.ALPHA, 1F).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.descLogin, View.ALPHA, 1F).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailLogin, View.ALPHA, 1F).setDuration(500)
        val editEmail = ObjectAnimator.ofFloat(binding.emailLayout, View.ALPHA, 1F).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordLogin, View.ALPHA, 1F).setDuration(500)
        val editPassword = ObjectAnimator.ofFloat(binding.passwordLayout, View.ALPHA, 1F).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1F).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title,message,email,editEmail,password,editPassword,button)
            start()
        }
    }
    private fun setupAction(){
        binding.buttonLogin.setOnClickListener {
            val email = binding.emailText.text.toString()
            val password = binding.passwordText.text.toString()

            viewModel.login(email,password).observe(this){user->
                when(user){
                    is Result.Success -> {
                        binding.loginProgress.visibility = View.INVISIBLE
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeay!")
                                setMessage("Anda berhasil login. Selamat berjelajah di StoryMeow!")
                                setPositiveButton("Lanjut") { _, _ ->
                                   saveSession(
                                       UserModel(
                                           user.data.loginResult.token,
                                           user.data.loginResult.name,
                                           user.data.loginResult.userId,
                                           true
                                       )
                                   )
                                }
                                Log.e("login",user.data.loginResult.token)
                                create()
                                show()
                            }
                    }
                    is Result.Loading ->{
                        binding.loginProgress.visibility = View.VISIBLE
                    }
                    is Result.Error ->{
                        binding.loginProgress.visibility = View.INVISIBLE
                        val error = user.error
                        Toast.makeText(this,error,Toast.LENGTH_SHORT).show()
                    }
                else -> {
                    Toast.makeText(this,"Yah maaf,server kami sedang sibuk", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun saveSession(session: UserModel){
        lifecycleScope.launch {
            viewModel.saveSession(session)
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ViewModelFactory.clearInstance()
            startActivity(intent)
        }
    }
}