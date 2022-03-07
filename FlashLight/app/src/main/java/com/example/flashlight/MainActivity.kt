package com.example.flashlight

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.json.JSONObject.NULL

class MainActivity : AppCompatActivity() {
    private lateinit var imgBtn : ImageButton
    var isOn : Boolean = false
    var hasFlash : Boolean = false
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgBtn = findViewById(R.id.imgBtn)
        hasFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        imgBtn.setOnClickListener {
            if(hasFlash){
                if(isOn){
                    isOn = false
                    imgBtn.setImageResource(R.drawable.img)
                    offPlease()
                }
                else{
                    isOn = true
                    imgBtn.setImageResource(R.drawable.img_1)
                    onPlease()
                }
            }
            else{
                Toast.makeText(this, "No Flash light detect", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onPlease() {
        val camManager : CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        assert(camManager!= NULL)
        val id : String = camManager.cameraIdList[0]
        camManager.setTorchMode(id, true)
        Toast.makeText(this, "Flash light on", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun offPlease() {
        val camManager : CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        assert(camManager!=NULL)
        val id : String = camManager.cameraIdList[0]
        camManager.setTorchMode(id, false)
        Toast.makeText(this, "Flash light off", Toast.LENGTH_SHORT).show()
    }
}