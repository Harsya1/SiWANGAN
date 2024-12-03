package com.example.siwangan.Activity
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.example.siwangan.R
//import com.google.zxing.BarcodeFormat
//import com.google.zxing.BinaryBitmap
//import com.google.zxing.MultiFormatReader
//import com.google.zxing.RGBLuminanceSource
//import com.google.zxing.WriterException
//import com.google.zxing.common.HybridBinarizer
//import com.google.zxing.qrcode.QRCodeWriter
//import com.journeyapps.barcodescanner.ScanContract
//import com.journeyapps.barcodescanner.ScanOptions
//
//class QRGenerateActivity : Fragment() {
//
//    private lateinit var etInput: EditText
//    private lateinit var ivQRCode: ImageView
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.activity_qr_generate, container, false)
//
//        etInput = view.findViewById(R.id.et_input)
//        val btnGenerateQR: Button = view.findViewById(R.id.btn_generate_qr)
//        ivQRCode = view.findViewById(R.id.iv_qr_code)
//        val btnScanQR: Button = view.findViewById(R.id.btn_scan_qr)
//
//        btnGenerateQR.setOnClickListener {
//            val text = etInput.text.toString()
//            if (text.isNotEmpty()) {
//                generateQRCode(text, ivQRCode)
//            } else {
//                Toast.makeText(activity, "Input cannot be empty", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        btnScanQR.setOnClickListener {
//            scanQRCode()
//        }
//
//        return view
//    }
//
//    private fun generateQRCode(text: String, imageView: ImageView) {
//        val writer = QRCodeWriter()
//        try {
//            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
//            val width = bitMatrix.width
//            val height = bitMatrix.height
//            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
//            for (x in 0 until width) {
//                for (y in 0 until height) {
//                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
//                }
//            }
//            imageView.setImageBitmap(bmp)
//        } catch (e: WriterException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun scanQRCode() {
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
//
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
//
//    companion object {
//        private const val CAMERA_REQUEST_CODE = 101
//    }
//}