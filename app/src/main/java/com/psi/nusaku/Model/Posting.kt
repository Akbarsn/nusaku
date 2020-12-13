package com.psi.nusaku.Model

data class Posting(
    val gambar: String?, val judul: String?, val tempat: String?, val deskripsi: String?,
    val tanggal: String?, val penulis: String?
) {
    constructor() : this("", "", "", "", "", "")
}
