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
import androidx.recyclerview.widget.RecyclerView
import com.seif.todoit.R
import com.seif.todoit.databinding.FragmentToDoListBinding
import com.seif.todoit.ui.adapters.TodoListAdapter
import com.seif.todoit.ui.veiwmodels.ShareViewModel
import com.seif.todoit.ui.veiwmodels.TodoViewModel


class ToDoListFragment : Fragment() {
    private var _binding: FragmentToDoListBinding? = null
    private val binding get() = _binding!!
    private val todoListAdapter: TodoListAdapter by lazy { TodoListAdapter() }
    lateinit var todoViewModel: TodoViewModel
    lateinit var shareViewModel: ShareViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentToDoListBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set menu
        setHasOptionsMenu(true)
        shareViewModel = ViewModelProvider(requireActivity())[ShareViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
        todoViewModel.getAllToDos.observe(viewLifecycleOwner, Observer { data ->
            shareViewModel.checkDatabaseEmpty(data)
            todoListAdapter.setData(data)

        })
        shareViewModel.emptyDataBase.observe(viewLifecycleOwner, Observer {
            showEmptyDataBaseViews(it)
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = todoListAdapter

        binding.btnAddTodo.setOnClickListener {
            findNavController().navigate(R.id.action_toDoListFragment_to_addTodoFragment)
        }

    }
    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallBack = object :  SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)
                val itemToDelete =  todoListAdapter.todoList[viewHolder.adapterPosition]
                todoViewModel.deleteTodo(itemToDelete)
                Toast.makeText(requireContext(), "deleted successfully ${itemToDelete.title}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEmptyDataBaseViews(check: Boolean) {
        if (check) {
            binding.noTasksImage.visibility = View.VISIBLE
            binding.noTasksTxt.visibility = View.VISIBLE
        } else {
            binding.noTasksImage.visibility = View.GONE
            binding.noTasksTxt.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            confirmDeleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Ok") { _, _ ->
            todoViewModel.deleteAllToDos()
            Toast.makeText(requireContext(), "Delete all items successfully", Toast.LENGTH_SHORT)
                .show()
        }
        with(builder) {
            setNegativeButton("Cancel") { _, _ -> }
            setTitle("Delete Everything ?")
            setMessage("Are you sure you want to remove everything ?")
            create().show()
        }
    }

}
/** setHasOptionMenu()
 * Report that this fragment would like to participate in populating the options menu
 * if parameter of hasMenu equals to  true, the fragment has menu items to contribute.
 */
