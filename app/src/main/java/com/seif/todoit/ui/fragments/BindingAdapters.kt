package com.seif.todoit.ui.fragments

import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.seif.todoit.R

class BindingAdapters {
    companion object{
        @BindingAdapter("android:navigateToAddFragment")
        //@JvmStatic
        fun navigateToAddFragment(view:FloatingActionButton, check:Boolean){
            if(check){
                view.findNavController().navigate(R.id.action_toDoListFragment_to_addTodoFragment)
            }
        }
    }
}