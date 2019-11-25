package com.softbankrobotics.dx.peppercodescanner;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.vision.barcode.Barcode;

public class BarcodeReaderActivity extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener {
    private static final String TAG = BarcodeReaderActivity.class.getSimpleName();
    public static final String KEY_SCAN_OVERLAY_VISIBILITY = "key_scan_overlay_visibility";
    public static final String KEY_CAPTURED_BARCODE = "key_captured_barcode";
    public static final String KEY_CAPTURED_RAW_BARCODE = "key_captured_raw_barcode";
    private boolean scanOverlayVisibility = true;
    private BarcodeReaderFragment mBarcodeReaderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_reader);

        final Intent intent = getIntent();
        if (intent != null) {
            scanOverlayVisibility = intent.getBooleanExtra(KEY_SCAN_OVERLAY_VISIBILITY, true);
        }
        mBarcodeReaderFragment = attachBarcodeReaderFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        // Enables regular immersive mode.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private BarcodeReaderFragment attachBarcodeReaderFragment() {
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        BarcodeReaderFragment fragment = new BarcodeReaderFragment(scanOverlayVisibility);
        fragment.setListener(this);
        fragmentTransaction.replace(R.id.fm_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void onScanned(Barcode barcode) {
        if (mBarcodeReaderFragment != null) {
            mBarcodeReaderFragment.pauseScanning();
        }
        if (barcode != null) {
            Intent intent = new Intent();
            intent.putExtra(KEY_CAPTURED_BARCODE, barcode);
            intent.putExtra(KEY_CAPTURED_RAW_BARCODE, barcode.rawValue);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onScanError(String errorMessage) {
        Log.e(TAG, "Scan error: " + errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {
        Log.e(TAG, "Camera permission denied");
    }
}
