package com.example.storymeow.view.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.storymeow.R
import com.example.storymeow.data.Result
import com.example.storymeow.data.getImageUri
import com.example.storymeow.data.reduceFileImage
import com.example.storymeow.data.uriToFile
import com.example.storymeow.databinding.ActivityCameraBinding
import com.example.storymeow.view.ViewModelFactory
import com.example.storymeow.view.camera.CameraXActivity.Companion.CAMERAX_RESULT
import com.example.storymeow.view.home.HomeActivity

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<CameraViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){
            if (it){
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()){
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonCamera.setOnClickListener { startCamera() }
        binding.buttonUpload.setOnClickListener { uploadImage() }
        binding.buttonCamerax.setOnClickListener { startCameraX() }
    }
    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){
        if (it != null){
            currentImageUri = it
            showImage()
        }else{
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun startCamera(){
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){
        if (it){
            showImage()
        }
    }
    private fun showImage(){
        currentImageUri?.let {
            Log.d("Image URI", "show Image: $it")
            binding.imageCamera.setImageURI(it)
        }
    }
    private fun uploadImage(){
        currentImageUri?.let {
            val imageFile = uriToFile(it,this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.editDeskripsi.text.toString()

            viewModel.uploadImage(imageFile,description).observe(this){result->
                if (result != null){
                    when(result){
                        is Result.Loading->{
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success->{
                            binding.progressBar.visibility = View.INVISIBLE
                            Toast.makeText(this,result.data.message, Toast.LENGTH_SHORT).show()
                            showHome()
                        }
                        is Result.Error->{
                            binding.progressBar.visibility = View.INVISIBLE
                            Toast.makeText(this,result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }?: Toast.makeText(this,getString(R.string.empty_image), Toast.LENGTH_SHORT).show()
    }
    private fun startCameraX(){
        val intent = Intent(this,CameraXActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == CAMERAX_RESULT){
            currentImageUri = it.data?.getStringExtra(CameraXActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }
    private fun showHome(){
        val intent = Intent(this,HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
    companion object{
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}