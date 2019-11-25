# Pepper Code Scanner Library

This Android Library will help you to scan barcodes and QR codes using the Google Vision library.

## Getting Started


### Prerequisites

A robotified project for Pepper with QiSDK. Read the [documentation](https://developer.softbankrobotics.com/pepper-qisdk) if needed.

Barcodes and QR codes.

### Running the Sample Application

The project comes complete with two sample projects. You can clone the repository, open it in Android Studio, and run this directly onto a Robot.

The two samples demonstrate two ways of using the library:

* By requesting an intent, that will launch a dedicated activity for scanning: **app-sample-with-intent**
* By integrating a scanning fragment in your activity: **app-sample-with-fragment**

Full implementation details are available to see in those projects.

### Installing

#### Using JitPack package repository

[**Follow these instructions**](https://jitpack.io/#softbankrobotics-labs/pepper-code-scanner)

Make sure to replace 'Tag' by the number of the version of the library you want to use.

#### Importing as an .aar file

[**Download the latest compiled .aar**](pepper-code-scanner-root/pepper-code-scanner/compiled/pepper-code-scanner-1.0.0.aar)

In order to implement the library into your own project, you must build and install the .aar library, please follow this steps: 

1.  Build the `pepper-code-scanner` project either with Android Studio, or by running `./gradlew build` The output AAR file is located in **pepper-code-scanner > build > outputs > aar**.

2.  In your robotified project, add the compiled AAR file:
    * Click File > New > New Module.
    * Click Import .JAR/.AAR Package then click Next.
    * Enter the location of the compiled AAR or JAR file then click Finish.

3.    Make sure the library is listed at the top of your `settings.gradle` file:
```
include ':app',  ':pepper-code-scanner-1.0.0'
```

4.  Open the app module's  `build.gradle`  file and add a new line to the  `dependencies`  block as shown in the following snippet:
```
dependencies {
   implementation project(":pepper-code-scanner-1.0.0")
}
```

5.  Click  **Sync Project with Gradle Files**.


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