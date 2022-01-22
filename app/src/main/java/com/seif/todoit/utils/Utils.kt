package com.seif.todoit.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(activity: Activity) {
    val inputMethodManager: InputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocusedView: View? = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}
// windowToken:
// Retrieve a unique token identifying the window this view is attached to.
// currentFocus:
// Calls  on the Window of this Activity to return the currently focused view.
// HIDE_NOT_ALWAYS:
// Flag to indicate that the soft input window should normally be hidden,
// unless it was originally shown with  SHOW_FORCED