package com.example.hw3

import android.Manifest
import android.R.attr
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import android.R.attr.button
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import kotlin.math.roundToInt
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

class HsvActivity : AppCompatActivity() {

    lateinit var seekBarHue : SeekBar
    lateinit var seekBarSat : SeekBar
    lateinit var seekBarVal: SeekBar
    lateinit var textViewHue: TextView
    lateinit var textViewSat: TextView
    lateinit var textViewVal: TextView
    lateinit var colorSquare : View
    lateinit var hexColorText : TextView
    lateinit var modeButton: Button
    lateinit var locButton: Button
    lateinit var locationClient: FusedLocationProviderClient
    val hsv= floatArrayOf(0.0F, 0.0F, 0.0F)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the UI components
        seekBarHue = findViewById(R.id.seekBarRed)
        seekBarSat = findViewById(R.id.seekBarGreen)
        seekBarVal = findViewById(R.id.seekBarBlue)
        textViewHue = findViewById(R.id.textViewRed)
        textViewSat = findViewById(R.id.textViewGreen)
        textViewVal = findViewById(R.id.textViewBlue)
        colorSquare = findViewById(R.id.color_square)
        hexColorText = findViewById(R.id.textViewHexColor)
        modeButton = findViewById(R.id.modeButton)
        locButton = findViewById(R.id.locButton)

        val red=intent.extras?.getInt("Red")
        val green=intent.extras?.getInt("Green")
        val blue=intent.extras?.getInt("Blue")

        //put it outside!!
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ){ isGranted ->
                if (isGranted) {
                    locationClient = LocationServices.getFusedLocationProviderClient(this)
                    locationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            val lat= location?.latitude
                            val str= lat?.let { it1 -> getColorString(it1) }
                            val tRed= str?.substring(1,3)?.toInt()
                            val tGreen= str?.substring(3,5)?.toInt()
                            val tBlue= str?.substring(5,7)?.toInt()
                            if (tRed != null && tGreen!=null && tBlue!=null) {
                                Color.RGBToHSV(tRed,tGreen,tBlue,hsv)
                            }
                            seekBarHue.progress=(hsv[0]*100).toInt()
                            seekBarSat.progress=(hsv[1]*100).toInt()
                            seekBarVal.progress=(hsv[2]*100).toInt()
                            regenerateColor()
                        }
                } else {
                                val snackbar= Snackbar.make(locButton, "Location permissions are not granted", Snackbar.LENGTH_LONG)
                                snackbar.show()
                                snackbar.setAction("RETRY"){
                                    snackbar.dismiss()
                                }
                }
            }

        if (red != null&& green != null&& blue != null) {
            Color.RGBToHSV(red,green,blue,hsv)
        }
        // Initialize the square's color on onCreate()
        regenerateColor()

        setUpSeekbar(seekBarHue, textViewHue, resources.getString(R.string.Hue),36000)
        setUpSeekbar(seekBarSat, textViewSat, resources.getString(R.string.Sat),100)
        setUpSeekbar(seekBarVal, textViewVal, resources.getString(R.string.Val),100)

        modeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Hue", seekBarHue.progress.toFloat()/100)
            intent.putExtra("Sat", seekBarSat.progress.toFloat()/100)
            intent.putExtra("Val", seekBarVal.progress.toFloat()/100)
            startActivity(intent)
        }
        
        locButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    locationClient = LocationServices.getFusedLocationProviderClient(this)
                    locationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            val lat= location?.latitude
                            val str= lat?.let { it1 -> getColorString(it1) }
                            val tRed= str?.substring(1,3)?.toInt()
                            val tGreen= str?.substring(3,5)?.toInt()
                            val tBlue= str?.substring(5,7)?.toInt()
                            if (tRed != null && tGreen!=null && tBlue!=null) {
                                Color.RGBToHSV(tRed,tGreen,tBlue,hsv)
                            }
                            seekBarHue.progress=(hsv[0]*100).toInt()
                            seekBarSat.progress=(hsv[1]*100).toInt()
                            seekBarVal.progress=(hsv[2]*100).toInt()
                            regenerateColor()
                        }
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                    val snackbar= Snackbar.make(locButton, "Location permissions are not granted", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    snackbar.setAction("RETRY"){
                    snackbar.dismiss()
                }
            }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        }
    }
    private fun getLocColor(){

    }
    private fun checkPermission(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val snackbar= Snackbar.make(locButton, "Location permissions are not granted", Snackbar.LENGTH_LONG)
        snackbar.show()
        snackbar.setAction("RETRY"){
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            snackbar.dismiss()
        }
    }

    // enter location.latitude as a parameter
    private fun getColorString(latitude : Double) : String {
        return resources.getString(
            R.string.locationString,
            ((latitude % 1) * 100000).roundToInt().toString().padStart(6, '0')
        )
    }

    private fun map(str:String):Int {
        if(str=="Hue") return 0
        if(str=="Saturation") return 1
        if(str=="Value") return 2
        return -1
    }

    private fun initialSetUp(sb: SeekBar, tv: TextView, color: String) {
        // Set initial color to random number
        sb.progress = (hsv[map(color)]*100).toInt()
        tv.text = color+":"+((hsv[map(color)]*10).toInt().toFloat()/10).toString() //round to 0.1
        updateSeekBarTextView(tv, color, sb.progress.toFloat()/100)
    }

    private fun setUpSeekbar(sb: SeekBar, tv: TextView, color : String, max: Int) {
        // Set the max value of seekbar to max hexcode - 255
        sb.max = max
        initialSetUp(sb, tv, color)

        sb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var t=sb.progress.toFloat()/100
                hsv[map(color)]= t

                regenerateColor()

                // Set TextView based on orientation
                updateSeekBarTextView(tv, color, t)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    // Modifies text label next to SeekBar depending on device orientation
    private fun updateSeekBarTextView(tv: TextView, color: String, progress: Float) {
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                tv.text = color
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                tv.text = color+":"+((progress*10).toInt().toFloat()/10).toString()
            }
        }
    }

    // Regenerates the color of the color square.
    private fun regenerateColor() {
        colorSquare.setBackgroundColor(
            Color.HSVToColor(hsv)
        )
        hexColorText.isInvisible=true
    }
}