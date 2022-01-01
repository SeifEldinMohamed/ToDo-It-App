package com.seif.todoit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
        // set menu
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_list_menu, menu)
    }


}

/** setHasOptionMenu()
 * Report that this fragment would like to participate in populating
 * the options menu
 * if parameter of hasMenu equals to  true, the fragment has menu items to contribute.
 */