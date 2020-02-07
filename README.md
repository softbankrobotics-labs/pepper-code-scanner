# Pepper Code Scanner Library

This Android Library will help you to scan barcodes and QR codes using the Google Vision library.

### Video Demonstration

This video was filmed at SoftBank Robotics Europe, and shows the basic use cases for this library. 

[Watch video on YouTube](https://youtu.be/qSOL2kl2T4w)

## Getting Started

### Prerequisites

Barcodes and QR codes are needed.

### Running the Sample Application

The project comes complete with two sample projects. You can clone the repository, open it in Android Studio, and run this directly onto a Robot.

The two samples demonstrate two ways of using the library:

* By requesting an intent, that will launch a dedicated activity for scanning: **app-sample-with-intent**
* By integrating a scanning fragment in your activity: **app-sample-with-fragment**

Full implementation details are available to see in those projects.

### Installing

[**Follow these instructions**](https://jitpack.io/#softbankrobotics-labs/pepper-code-scanner)

Make sure to replace 'Tag' by the number of the version of the library you want to use.


## Usage

*This README assumes some standard setup can be done by the user, such as initialising variables or implementing code in the correct functions. Refer to the Sample Project for full usage code.*

You can launch the barcode scanner as an Activity with the following code: 
```
val launchIntent = Intent(this, BarcodeReaderActivity::class.java)
startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST)
```
The Activity will close once a code has been read. You will be able to get the result by overriding the onActivityResult function:
```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode != Activity.RESULT_OK) {
        Log.e(TAG, "Scan error")
        return
    }

    if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
        val barcode: Barcode? = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE)
        result.text = barcode?.rawValue ?: ""
    }
}
```
Here we display the result in a TextView.

You can also launch the barcode scanner in a Fragment. You'll first have to make your Activity implements BarcodeReaderFragment.BarcodeReaderListener, and then use the following code to add the Fragment:
```
val readerFragment = BarcodeReaderFragment()
readerFragment.setListener(this)
val fragmentTransaction = supportFragmentManager.beginTransaction()
fragmentTransaction.replace(R.id.fm_container, readerFragment)
fragmentTransaction.commitAllowingStateLoss()
```
The result of the scan will be given in the onScanned callback. You'll have to override it:
```
override fun onScanned(barcode: Barcode?) {
    result.text = barcode?.rawValue ?: ""

    // Remove Fragment
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    val fragmentId = supportFragmentManager.findFragmentById(R.id.fm_container)
    if (fragmentId != null) {
        fragmentTransaction.remove(fragmentId)
    }
    fragmentTransaction.commitAllowingStateLoss()
}
```
Here we display the result in a TextView and we remove the Fragment as we don't need it anymore.

By default, a scanner overlay is displayed for UX purposes. If you launch the barcode scanner as an Activity, you can disable the scanner overlay in the Intent used to launch the Activity:
```
val launchIntent = Intent(this, BarcodeReaderActivity::class.java)
launchIntent.putExtra(KEY_SCAN_OVERLAY_VISIBILITY, false)
startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST)
```
With KEY_SCAN_OVERLAY_VISIBILITY = "key_scan_overlay_visibility"

If you launch the barcode scanner in a Fragment, you can remove the scanner overlay this way:
```
val readerFragment = BarcodeReaderFragment(false)
readerFragment.setListener(this)
val fragmentTransaction = supportFragmentManager.beginTransaction()
fragmentTransaction.replace(R.id.fm_container, readerFragment)
fragmentTransaction.commitAllowingStateLoss()
```


## Known limitations

Because of the resolution and depth of field of the camera, it may be difficult to read small barcodes.


## License

This project is licensed under the BSD 3-Clause "New" or "Revised" License- see the [LICENSE](LICENSE.md) file for details.