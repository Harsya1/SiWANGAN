package com.example.siwangan.Activity.Admin

class AdminHasilScanQRActivity {
}

//private fun scanQRCode() {
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
//        } else {
//            val options = ScanOptions()
//            options.setPrompt("Scan a QR code")
//            options.setBeepEnabled(false)
//            options.setBarcodeImageEnabled(true)
//            options.setOrientationLocked(true) // Lock orientation to portrait
//            barcodeLauncher.launch(options)
//        }
//    }

//    private fun decodeQRCodeFromBitmap(bitmap: Bitmap) {
//        val intArray = IntArray(bitmap.width * bitmap.height)
//        bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//        val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
//        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
//        try {
//            val result = MultiFormatReader().decode(binaryBitmap)
//            etInput.setText(result.text)
//            Toast.makeText(activity, "Scanned: ${result.text}", Toast.LENGTH_LONG).show()
//        } catch (e: Exception) {
//            Toast.makeText(activity, "Failed to decode QR code", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == CAMERA_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            scanQRCode()
//        } else {
//            Toast.makeText(activity, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
//        if (result.contents != null) {
//            etInput.setText(result.contents)
//            Toast.makeText(activity, "Scan Berhasil", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(activity, "Scan failed", Toast.LENGTH_SHORT).show()
//        }
//    }