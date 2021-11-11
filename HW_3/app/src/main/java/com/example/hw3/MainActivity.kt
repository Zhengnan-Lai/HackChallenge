package com.example.hw3

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
import androidx.core.view.isInvisible


class MainActivity : AppCompatActivity() {
    private val max = 255

    lateinit var seekBarRed : SeekBar
    lateinit var seekBarBlue : SeekBar
    lateinit var seekBarGreen: SeekBar
    lateinit var textViewRed: TextView
    lateinit var textViewBlue: TextView
    lateinit var textViewGreen: TextView
    lateinit var colorSquare : View
    lateinit var hexColorText : TextView
    lateinit var modeButton: Button
    lateinit var locButton: Button
    var colorVal=0
    val hsv= floatArrayOf(0.0F, 0.0F, 0.0F)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the UI components
        seekBarRed = findViewById(R.id.seekBarRed)
        seekBarGreen = findViewById(R.id.seekBarGreen)
        seekBarBlue = findViewById(R.id.seekBarBlue)
        textViewRed = findViewById(R.id.textViewRed)
        textViewGreen = findViewById(R.id.textViewGreen)
        textViewBlue = findViewById(R.id.textViewBlue)
        colorSquare = findViewById(R.id.color_square)
        hexColorText = findViewById(R.id.textViewHexColor)
        modeButton = findViewById(R.id.modeButton)
        locButton = findViewById(R.id.locButton)

        locButton.text="Send Color"
        if(intent.extras?.getFloat("Hue")!=null) hsv[0] = intent.extras?.getFloat("Hue")!!
        if(intent.extras?.getFloat("Sat")!=null) hsv[1] = intent.extras?.getFloat("Sat")!!
        if(intent.extras?.getFloat("Val")!=null) hsv[2] = intent.extras?.getFloat("Val")!!

        colorVal=Color.HSVToColor(hsv)


        setUpSeekbar(seekBarRed, textViewRed, resources.getString(R.string.red))
        setUpSeekbar(seekBarGreen, textViewGreen, resources.getString(R.string.green))
        setUpSeekbar(seekBarBlue, textViewBlue, resources.getString(R.string.blue))

        modeButton.setOnClickListener {
            val intent = Intent(this, HsvActivity::class.java)
            intent.putExtra("Red", seekBarRed.progress)
            intent.putExtra("Green", seekBarGreen.progress)
            intent.putExtra("Blue", seekBarBlue.progress)
            startActivity(intent)
        }

        // Initialize the square's color on onCreate()
        regenerateColor()

        locButton.setOnClickListener {
            val intent=Intent()
            val info=resources.getString(
                R.string.hexString,
                Integer.toHexString(seekBarRed.progress).toUpperCase(),
                Integer.toHexString(seekBarGreen.progress).toUpperCase(),
                Integer.toHexString(seekBarBlue.progress).toUpperCase()
            )
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "Check out this color: "+info)
            intent.type="text/plain"
            startActivity(intent)
        }
    }


    private fun initialSetUp(sb: SeekBar, tv: TextView, color: String) {
        // Set initial color to random number
        var num=0
        if(color=="Red") num=Color.red(colorVal)
        if(color=="Green") num=Color.green(colorVal)
        if(color=="Blue") num=Color.blue(colorVal)
        sb.progress = num
        updateSeekBarTextView(tv,color,num)
    }

    private fun setUpSeekbar(sb: SeekBar, tv: TextView, color : String) {
        // Set the max value of seekbar to max hexcode - 255
        sb.max = max
        initialSetUp(sb, tv, color)

        sb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                regenerateColor()

                // Set TextView based on orientation
                updateSeekBarTextView(tv, color, p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    // Modifies text label next to SeekBar depending on device orientation
    private fun updateSeekBarTextView(tv: TextView, color: String, progress: Int) {
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                tv.text = color
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                tv.text = resources.getString(R.string.seekbarLabel, color, progress)
            }
        }
    }

    // Regenerates the color of the color square.
    private fun regenerateColor() {
        colorSquare.setBackgroundColor(
            Color.rgb(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress)
        )

        hexColorText.text = resources.getString(
            R.string.hexString,
            Integer.toHexString(seekBarRed.progress).toUpperCase(),
            Integer.toHexString(seekBarGreen.progress).toUpperCase(),
            Integer.toHexString(seekBarBlue.progress).toUpperCase()
        )

    }
}