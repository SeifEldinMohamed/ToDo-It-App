package com.seif.todoit.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.seif.todoit.R
import com.seif.todoit.data.models.TodoModel
import com.seif.todoit.databinding.FragmentAddTodoBinding
import com.seif.todoit.ui.veiwmodels.ShareViewModel
import com.seif.todoit.ui.veiwmodels.TodoViewModel


class AddTodoFragment : Fragment() {
    private lateinit var binding : FragmentAddTodoBinding
    private lateinit var todoViewModel:TodoViewModel
    private lateinit var sharedViewModel:ShareViewModel
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
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[ShareViewModel::class.java]
        // set Menu
        setHasOptionsMenu(true)
        binding.prioritySpinner.onItemSelectedListener = sharedViewModel.listener

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add){
            insertDataToDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDatabase() {
        val todoTitle = binding.titleEdit.text.toString()
        val priority = binding.prioritySpinner.selectedItem.toString()
        val todoDescription = binding.descriptionEdit.text.toString()
       if(sharedViewModel.validateTodo(todoTitle, todoDescription)){
           // insert in database
           val todo = TodoModel(
               0,
               todoTitle,
              sharedViewModel.getPriority(priority),
               todoDescription
           )
           todoViewModel.insertTodo(todo)
           Toast.makeText(context, "successfully added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addTodoFragment_to_toDoListFragment)
       }
        else{
           Toast.makeText(context, "please fill out all the fields", Toast.LENGTH_SHORT).show()
       }
    }


}