package com.example.appinstagram.lib_base.utils
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyBoardUtils {
    //Hiện bàn phím
    fun showKeyboard(view: View) {
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null) {
            view.requestFocus()
            imm.showSoftInput(view, 0)
        }
    }
    // Ẩn bàn phím
    fun hideKeyboard(view: View) {
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
    //Toggle Bàn phím
    fun toggleSoftInput(view: View) {
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.toggleSoftInput(0, 0)
    }
}