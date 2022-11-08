package com.example.textextractorfromimage

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.textextractorfromimage.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val TAG = "MAIN_TAG"
    private var imageUri: Uri? = null

    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 101

    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>

    private lateinit var progressDialog: ProgressDialog

    private lateinit var textRecognizer:TextRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraPermission =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        binding.captureBtn.setOnClickListener(View.OnClickListener {
            showInputImageDialog()
        })

        binding.recogBtn.setOnClickListener(View.OnClickListener {
            if(imageUri == null){
                Toast.makeText(this, "Pick image first", Toast.LENGTH_SHORT).show()
            } else{
                recognizeTextFromImage()
            }
        })
    }

    private fun recognizeTextFromImage() {
        Log.d(TAG, "recognizeTextFromImage: ")
        progressDialog.setMessage("Preparing text...")
        progressDialog.show()

        try{
            var inputImage:InputImage = InputImage.fromFilePath(this, imageUri!!)
            progressDialog.setMessage("Recognizing text...")
            var textTaskResult: Task<Text> = textRecognizer.process(inputImage).addOnSuccessListener {
                progressDialog.dismiss()
                var recognizedText = it.text
                Log.d(TAG, "recognizeTextFromImage: recognizedText: $recognizedText")
                binding.resultTV.text = recognizedText
            }.addOnFailureListener {
                progressDialog.dismiss()
                Log.d(TAG, "recognizeTextFromImage: $it")
                Toast.makeText(this, "Failed recognizing text due to "+it.message, Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            progressDialog.dismiss()
            Toast.makeText(this, "Failed preparing image due to "+e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showInputImageDialog() {
        var popupMenu:PopupMenu = PopupMenu(this, binding.captureBtn)

        popupMenu.menu.add(Menu.NONE, 1,1,"CAMERA")
        popupMenu.menu.add(Menu.NONE, 2,1,"GALLERY")

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { p0 ->
            var id = p0!!.itemId
            if (id == 1) {
                Log.d(TAG, "showInputImageDialog: Camera clicked...")
                if (checkCameraPermission()) {
                    pickImageCamera()
                } else {
                    requestCameraPermission()
                }
            } else if (id == 2) {
                Log.d(TAG, "showInputImageDialog: Gallery clicked")
                if (checkStoragePermission()) {
                    pickImageGallery()
                } else {
                    requestStoragePermission()
                }
            }
            true
        }
    }

    private fun pickImageGallery() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
    }

    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> {
                if (it.resultCode == Activity.RESULT_OK) {
                    // Image picked
                    val data: Intent = it.data!!
                    imageUri = data.data
                    Log.d(TAG, "onActivityResult: imageUri $imageUri")
                    // Set to imageview
                    binding.myImage.setImageURI(imageUri)
                } else {
                    Log.d(TAG, "onActivityResult: cancelled")
                    Toast.makeText(this, "Cancelled...", Toast.LENGTH_SHORT).show()
                }
            })

    private fun pickImageCamera() {
        var values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Sample Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        cameraActivityResultLauncher.launch(intent)
    }

    private var cameraActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult>() {
                if (it.resultCode == Activity.RESULT_OK) {
                    // Image is taken from camera
                    // We already have image in imageUri using function pickImageCamera()
                    Log.d(TAG, "onActivityResult: imageUri $imageUri")
                    binding.myImage.setImageURI(imageUri)
                } else {
                    Log.d(TAG, "onActivityResult: cancelled")
                    Toast.makeText(this, "Cancelled...", Toast.LENGTH_SHORT).show()
                }
            })

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED)
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE)
    }

    private fun checkCameraPermission(): Boolean {
        var cameraResult = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == (PackageManager.PERMISSION_GRANTED)
        var storageResult = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED)

        return cameraResult && storageResult
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    var cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    var storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted) {
                        pickImageCamera()
                    } else {
                        Toast.makeText(
                            this,
                            "Camera and storage permissions are required",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    var storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        pickImageGallery()
                    } else {
                        Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}