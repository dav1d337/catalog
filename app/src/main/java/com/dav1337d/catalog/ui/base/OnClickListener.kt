package com.dav1337d.catalog.ui.base

interface OnClickListener<in V> {
    fun onCheckBoxClick(item: V) { }
    fun onSaveClick(item: V, rating: Int, date: String, comment: String) { }
    fun onLongPress(item: V) { }
}