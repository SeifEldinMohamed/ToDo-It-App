package com.seif.todoit.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.seif.todoit.R
import com.seif.todoit.databinding.FragmentAddTodoBinding


class AddTodoFragment : Fragment() {
    lateinit var binding : FragmentAddTodoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set Menu
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }
}