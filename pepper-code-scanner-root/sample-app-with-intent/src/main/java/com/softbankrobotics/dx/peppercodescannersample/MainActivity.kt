package com.softbankrobotics.dx.peppercodescannersample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.barcode.Barcode
import com.softbankrobotics.dx.peppercodescanner.BarcodeReaderActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val BARCODE_READER_ACTIVITY_REQUEST = 1208
        private const val KEY_MESSAGE = "key_message"
        private const val KEY_SCAN_OVERLAY_VISIBILITY = "key_scan_overlay_visibility"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout.setOnClickListener {
            val launchIntent = Intent(this, BarcodeReaderActivity::class.java)
            // Uncomment the next line to remove the scanner overlay
            //launchIntent.putExtra(KEY_SCAN_OVERLAY_VISIBILITY, false)
            startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST)
        }
    }

    override fun onStart() {
        super.onStart()
        hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            Log.e(TAG, "Scan error")
            return
        }

        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
            val barcode: Barcode? =
                data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE)
            val message = "Scan result: ${barcode?.rawValue}"

            val launchIntent = Intent(this, ResultActivity::class.java)
            launchIntent.putExtra(KEY_MESSAGE, message)
            startActivity(launchIntent)
        }
    }
}
