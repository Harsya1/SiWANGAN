package com.example.siwangan.Helper

object ImageCache {

    // Variabel untuk menyimpan gambar dalam format Base64
    var base64Image: String? = null

    /**
     * Menyimpan gambar Base64 ke dalam cache.
     * @param base64 Gambar dalam format Base64.
     */
    fun saveBase64Image(base64: String) {
        base64Image = base64
    }

    /**
     * Mengambil gambar Base64 dari cache.
     * Setelah dipanggil, cache akan dihapus untuk membebaskan memori.
     * @return Gambar dalam format Base64, atau null jika tidak ada gambar.
     */
    fun getBase64ImageAndClear(): String? {
        val tempImage = base64Image
        base64Image = null // Hapus cache setelah diakses
        return tempImage
    }

    /**
     * Menghapus gambar yang disimpan dalam cache.
     */
    fun clearCache() {
        base64Image = null
    }
}
