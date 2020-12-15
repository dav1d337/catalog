package com.dav1337d.catalog.model.books

import android.graphics.Bitmap

data class BookSearchResponse constructor(val kind: String, val totalItems: Int, val items: List<BookItem>)

data class BookItem constructor(
    val kind: String,
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo,
    val accessInfo: AccessInfo,
    val searchInfo: SearchInfo,
    var thumbnail: Bitmap,
    var read: Boolean = false
)

data class VolumeInfo constructor(
    val title: String,
    val subtitle: String,
    val authors: List<String>?,
    val publisher: String,
    val publishedDate: String?,
    val description:String,
    val industryIdentifiers: List<Map<String, String>>,
    val readingModes: Map<String, String>,
    val pageCount: Int,
    val printType: String,
    val averageRating: Double,
    val ratingsCount: Int,
    val maturityRating: String,
    val allowAnonLogging: Boolean,
    val contentVersion: String,
    val panelizationSummary: Map<String, Boolean>,
    val imageLinks: Map<String, String>?,
    val language: String,
    val previewLink: String,
    val infoLink: String,
    val canonicalVolumeLink: String
)

data class SaleInfo constructor(
    val country: String,
    val saleability: String,
    val isEbook: Boolean,
    val listPrice: Map<String, String>,
    val retailPrice: Map<String,String>,
    val buyLink: String
)

data class AccessInfo constructor(
    val country: String,
    val viewability: String,
    val embeddable: Boolean,
    val publicDomain: Boolean,
    val textToSpeechPermission: String,
    val epub: Map<String, Boolean>,
    val pdf: Map<String, Boolean>,
    val webReaderLink: String,
    val accessViewStatus: String,
    val quoteSharingAllowed: Boolean
)

data class SearchInfo constructor(val textSnippet: String)