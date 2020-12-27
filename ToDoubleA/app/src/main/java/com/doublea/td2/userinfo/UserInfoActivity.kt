package com.doublea.td2.userinfo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.doublea.td2.R
import com.doublea.td2.task.TaskActivity
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        findViewById<Button>(R.id.take_picture_button).setOnClickListener {
            askCameraPermissionAndOpenCamera()
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



    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        //handleImage(tmpFile.toUri())
    }

    // use
    private fun openCamera() = takePicture.launch()

    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )

}