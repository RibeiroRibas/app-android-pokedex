package br.com.egsys.ui.extensions

import android.app.Activity
import android.widget.Toast

fun Activity.showError(mesage: String) {
    Toast.makeText(
        this,
        mesage,
        Toast.LENGTH_LONG
    ).show()
}