package com.softbankrobotics.dx.peppercodescanner;

import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Generic tracker which is used for tracking or reading a barcode (and can really be used for
 * any type of item).  This is used to receive newly detected items, add a graphical representation
 * to an overlay, update the graphics as the item changes, and remove the graphics when the item
 * goes away.
 */
class BarcodeGraphicTracker extends Tracker<Barcode> {
    private final BarcodeGraphicTrackerListener listener;

    BarcodeGraphicTracker(BarcodeGraphicTrackerListener listener) {
        this.listener = listener;
    }

    /**
     * Start tracking the detected item instance within the item overlay.
     */
    @Override
    public void onNewItem(int id, Barcode item) {
        Log.e("XX", "barcode detected: " + item.displayValue + ", listener: " + listener);

        if (listener != null) {
            listener.onScanned(item);
        }
    }

    /**
     * Hide the graphic when the corresponding object was not detected.  This can happen for
     * intermediate frames temporarily, for example if the object was momentarily blocked from
     * view.
     */
    @Override
    public void onMissing(Detector.Detections<Barcode> detectionResults) {
    }

    /**
     * Called when the item is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    @Override
    public void onDone() {
    }

    public interface BarcodeGraphicTrackerListener {
        void onScanned(Barcode barcode);

        void onScanError(String errorMessage);
    }
}
