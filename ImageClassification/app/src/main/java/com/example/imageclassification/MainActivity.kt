package com.example.imageclassification

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.imageclassification.databinding.ActivityMainBinding
import com.example.imageclassification.ml.BirdModel
import org.tensorflow.lite.support.image.TensorImage
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityMainBinding
    val GALLERY_RESULT_CODE = 123
    private lateinit var tts: TextToSpeech
    private var text:String = "Hello"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TextToSpeech(this, this)

        binding.takeImgBtn.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                takePicturePreview.launch(null)
            } else {
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        })

        binding.loadImgBtn.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                onresult.launch(intent)
            } else {
                requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        })

        binding.resultTv.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/search?q=${binding.resultTv.text}")
                )
            )
        })
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                takePicturePreview.launch(null)
            } else {
                Toast.makeText(this, "Permission Denied! Try again", Toast.LENGTH_SHORT).show()
            }
        }

    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                binding.myImage.setImageBitmap(it)
                outputGenerator(it)
            }
        }

    private val onresult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.i("TAG", "This is the result: ${it.data} ${it.resultCode}")
            onResultReceived(GALLERY_RESULT_CODE, it)
        }

    private fun onResultReceived(galleryResultCode: Int, it: ActivityResult?) {
        when (galleryResultCode) {
            GALLERY_RESULT_CODE -> {
                if (it?.resultCode == Activity.RESULT_OK) {
                    it.data?.data?.let {
                        Log.i("TAG", "onResultReceived: $it")
                        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
                        binding.myImage.setImageBitmap(bitmap)
                        outputGenerator(bitmap)
                    }
                } else {
                    Log.e("TAG", "onActivityResult: error in selecting image")
                }
            }
        }
    }

    private fun outputGenerator(bitmap: Bitmap) {
        val model = BirdModel.newInstance(this)

        // Converting bitmap into tensor flow image
        val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val image = TensorImage.fromBitmap(bitmap)

        // Process the image using trained model and sort it in descending order
        val outputs = model.process(image).probabilityAsCategoryList.apply {
            sortByDescending { it.score }
        }

        // Getting result having high probability
        val highProbabilityOutput = outputs[0]

        //Setting output text
        binding.resultTv.text = highProbabilityOutput.label
        binding.searchTxt.visibility = View.VISIBLE
        text = highProbabilityOutput.label
        speak()
        Log.i("TAG", "outputGenerator: $highProbabilityOutput")
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                AlertDialog.Builder(this).setTitle("Download Image?")
                    .setMessage("Do you want to download this image to your device?")
                    .setPositiveButton("Yes") { _, _ ->
                        val drawable: BitmapDrawable = binding.myImage.drawable as BitmapDrawable
                        val bitmap = drawable.bitmap
                        downloadImage(bitmap)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            } else {
                Toast.makeText(
                    this,
                    "Please allow permission to download image",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    // For downloading image
    private fun downloadImage(bitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "Birds_Images" + System.currentTimeMillis() / 1000
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        if (uri != null) {
            contentResolver.insert(uri, contentValues)?.also {
                contentResolver.openOutputStream(it).use { outputStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                        throw IOException("Couldn't save the image")
                    } else {
                        Toast.makeText(applicationContext, "Image saved", Toast.LENGTH_SHORT).show()
                    }
                }
                return it
            }
        }
        return null
    }

    // For speaking
    private fun speak() {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    override fun onInit(p0: Int) {
        if (p0 === TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Log.e("TTS", "This Language is not supported")
            } else {
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    override fun onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy()
    }

}