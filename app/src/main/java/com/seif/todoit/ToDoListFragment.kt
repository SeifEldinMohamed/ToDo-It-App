package com.seif.todoit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.seif.todoit.databinding.FragmentToDoListBinding


class ToDoListFragment : Fragment() {
    lateinit var binding: FragmentToDoListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentToDoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddTodo.setOnClickListener {
            findNavController().navigate(R.id.action_toDoListFragment_to_addTodoFragment)
        }
        binding.constraintLayout.setOnClickListener {
            findNavController().navigate(R.id.action_toDoListFragment_to_updateTodoFragment)
        }
    }


}