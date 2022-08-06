package br.com.ribeiroribas.ui.extensions

import android.widget.ImageView
import coil.load

fun ImageView.onLoadImage(url: String? = null) {
    load(url) {
        fallback(br.com.ribeiroribas.R.drawable.erro)
        error(br.com.ribeiroribas.R.drawable.erro)
        placeholder(br.com.ribeiroribas.R.drawable.small_pokeball)
    }
}