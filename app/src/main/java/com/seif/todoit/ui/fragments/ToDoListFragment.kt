package com.seif.todoit.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.seif.todoit.R
import com.seif.todoit.data.models.TodoModel
import com.seif.todoit.databinding.FragmentToDoListBinding
import com.seif.todoit.ui.adapters.TodoListAdapter
import com.seif.todoit.ui.veiwmodels.ShareViewModel
import com.seif.todoit.ui.veiwmodels.TodoViewModel
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator


class ToDoListFragment : Fragment() {
    lateinit var binding :FragmentToDoListBinding
    private val todoListAdapter: TodoListAdapter by lazy { TodoListAdapter() }
    lateinit var todoViewModel: TodoViewModel
    lateinit var shareViewModel: ShareViewModel
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

        shareViewModel = ViewModelProvider(requireActivity())[ShareViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
        // set menu
        setHasOptionsMenu(true)

        todoViewModel.getAllToDos.observe(viewLifecycleOwner, Observer { data ->
            shareViewModel.checkDatabaseEmpty(data)
            todoListAdapter.setData(data)
        })
        shareViewModel.emptyDataBase.observe(viewLifecycleOwner, Observer {
            showEmptyDataBaseViews(it)
        })

        setUpRecyclerView()
        swipeToDelete(binding.recyclerView)

        binding.btnAddTodo.setOnClickListener {
            findNavController().navigate(R.id.action_toDoListFragment_to_addTodoFragment)
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

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = todoListAdapter
        binding.recyclerView.itemAnimator = ScaleInTopAnimator().apply {
            addDuration = 200
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack: SwipeToDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = todoListAdapter.todoList[viewHolder.adapterPosition]
                // delete item
                todoViewModel.deleteTodo(deletedItem)
                // restore (undo)
                restoreDeletedItem(viewHolder.itemView, deletedItem, viewHolder.adapterPosition)
            }
        }
        // attach item touch helper to recyclerView
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun restoreDeletedItem(view: View, deletedItem: TodoModel, position: Int) {
        val snackBar = Snackbar.make(
            view,
            "${deletedItem.title} todo deleted successfully",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            todoViewModel.insertTodo(deletedItem)
        }.show()
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
