package com.seif.todoit.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
            // handle appearance of no tasks image and text
            if(data.isEmpty()){
                binding.noTasksImage.visibility = View.VISIBLE
                binding.noTasksTxt.visibility = View.VISIBLE
            }
            else{
                binding.noTasksImage.visibility = View.GONE
                binding.noTasksTxt.visibility = View.GONE
            }
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = todoListAdapter


        binding.btnAddTodo.setOnClickListener {
            findNavController().navigate(R.id.action_toDoListFragment_to_addTodoFragment)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete_all){
            confirmDeleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Ok"){ _,_ ->
            todoViewModel.deleteAllToDos()
            Toast.makeText(requireContext(), "Delete all items successfully", Toast.LENGTH_SHORT).show()
        }
        with(builder){
            setNegativeButton("Cancel"){_,_ ->}
            setTitle("Delete Everything ?")
            setMessage("Are you sure you want to remove everything ?")
            create().show()
        }
    }


}

/** setHasOptionMenu()
 * Report that this fragment would like to participate in populating
 * the options menu
 * if parameter of hasMenu equals to  true, the fragment has menu items to contribute.
 */