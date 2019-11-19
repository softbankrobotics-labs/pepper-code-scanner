package com.softbankrobotics.dx.peppercodescannerfragmentsample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.barcode.Barcode
import com.softbankrobotics.dx.peppercodescanner.BarcodeReaderFragment

class MainActivity : AppCompatActivity(), BarcodeReaderFragment.BarcodeReaderListener {

    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_MESSAGE = "key_message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val readerFragment = BarcodeReaderFragment()
        // Change the previous line with the commented one
        // if you don't want to display the scanner overlay
        //val readerFragment = BarcodeReaderFragment(false)
        readerFragment.setListener(this)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fm_container, readerFragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()
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

    override fun onScanned(barcode: Barcode?) {
        Log.i(TAG, "Code scanned")

        val message = "Scan result: ${barcode?.rawValue}"

        val launchIntent = Intent(this, ResultActivity::class.java)
        launchIntent.putExtra(KEY_MESSAGE, message)
        startActivity(launchIntent)
    }

    override fun onScanError(errorMessage: String?) {
        Log.e(TAG, "Scan error: $errorMessage")
    }

    override fun onCameraPermissionDenied() {
        Log.e(TAG, "Camera permission denied")
    }
}
