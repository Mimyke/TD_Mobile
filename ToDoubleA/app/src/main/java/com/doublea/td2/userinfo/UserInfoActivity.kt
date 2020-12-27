package com.doublea.td2.userinfo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.doublea.td2.BuildConfig
import com.doublea.td2.R
import com.doublea.td2.task.TaskActivity
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {

    private val userModel: UserInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        findViewById<Button>(R.id.take_picture_button).setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }
        findViewById<Button>(R.id.upload_image_button).setOnClickListener {
            pickImage()
        }

    }

    private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) openCamera()
                else showExplanationDialog()
            }

    private fun requestCameraPermission() =
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }

    private fun handleImage(imageUri: Uri) = userModel.updateAvatar(convert(imageUri))

    // register
    /*private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        handleImage(tmpFile.toUri())
    }*/

    // create a temp file and get a uri for it
    private val photoUri by lazy {
        FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID +".fileprovider",
                File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    // register
    private val pickInGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) { handleImage(it) }

    // use
    private fun pickImage() = pickInGallery.launch("image/*")


    // use
    private fun openCamera() = takePicture.launch(photoUri)

    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )

}