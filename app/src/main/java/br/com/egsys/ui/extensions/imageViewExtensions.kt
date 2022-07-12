package br.com.egsys.ui.extensions

import android.widget.ImageView
import coil.load

fun ImageView.onLoadImage(url: String? = null) {
    load(url) {
        fallback(br.com.egsys.R.drawable.erro)
        error(br.com.egsys.R.drawable.erro)
        placeholder(br.com.egsys.R.drawable.small_pokeball)
    }
}