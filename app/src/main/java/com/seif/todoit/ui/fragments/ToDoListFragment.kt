package com.seif.todoit.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.seif.todoit.R
import com.seif.todoit.databinding.FragmentToDoListBinding
import com.seif.todoit.ui.adapters.TodoListAdapter
import com.seif.todoit.ui.veiwmodels.TodoViewModel


class ToDoListFragment : Fragment() {
    lateinit var binding: FragmentToDoListBinding
    private val todoListAdapter: TodoListAdapter by lazy { TodoListAdapter() }
    lateinit var todoViewModel: TodoViewModel
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
        // set menu
        setHasOptionsMenu(true)
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
        todoViewModel.getAllToDos.observe(viewLifecycleOwner, Observer { data ->
            todoListAdapter.setData(data)
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = todoListAdapter
        binding.noTasksImage.visibility = View.GONE
        binding.noTasksTxt.visibility = View.GONE

        binding.btnAddTodo.setOnClickListener {
            findNavController().navigate(R.id.action_toDoListFragment_to_addTodoFragment)
        }

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