package com.softbankrobotics.dx.peppercodescanner;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private final BarcodeGraphicTracker.BarcodeGraphicTrackerListener listener;

    BarcodeTrackerFactory(BarcodeGraphicTracker.BarcodeGraphicTrackerListener listener) {
        this.listener = listener;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        return new BarcodeGraphicTracker(listener);
    }

}

