package br.com.ribeiroribas.ui.extensions

import android.app.Activity
import android.widget.Toast

fun Activity.showError(mesage: String) {
    Toast.makeText(
        this,
        mesage,
        Toast.LENGTH_LONG
    ).show()
}