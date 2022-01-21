package com.seif.todoit.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.seif.todoit.R
import com.seif.todoit.data.models.TodoModel
import com.seif.todoit.databinding.FragmentToDoListBinding
import com.seif.todoit.ui.adapters.TodoListAdapter
import com.seif.todoit.ui.veiwmodels.ShareViewModel
import com.seif.todoit.ui.veiwmodels.TodoViewModel
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator


class ToDoListFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentToDoListBinding
    private val todoListAdapter: TodoListAdapter by lazy { TodoListAdapter() }
    lateinit var todoViewModel: TodoViewModel
    private lateinit var shareViewModel: ShareViewModel
    private var isNightMode: Boolean = false
    private lateinit var settingPref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
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
        requireActivity().invalidateOptionsMenu()
        settingPref = view.context.getSharedPreferences("settingPrefs", Context.MODE_PRIVATE)
        isNightMode = settingPref.getBoolean("nightMode", false)
        // check for dark mode theme
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

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
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as? SearchView
        searchView?.isSubmitButtonEnabled =
            true // Enables showing a submit button when the query is non-empty
        searchView?.setOnQueryTextListener(this)
    }

    // called when to change icon of dark mode according to the current mode state
    override fun onPrepareOptionsMenu(menu: Menu) {
        if (isNightMode) { // dark mode
            menu[1].setIcon(R.drawable.ic_light_mode)
        } else {
            menu[1].setIcon(R.drawable.ic_dark_mode)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmDeleteAll()
            R.id.theme -> {
                edit = settingPref.edit()
                if (isNightMode) { // if it was dark mode then activate the light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    edit.putBoolean("nightMode", false)
                    edit.apply()
                } else { // if it was light mode then activate the dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    edit.putBoolean("nightMode", true)
                    edit.apply()
                }
            }
            R.id.menu_priority_high -> todoViewModel.sortByPriorityHigh().observe(this, Observer { todoListAdapter.setData(it) })
            R.id.menu_priority_low -> todoViewModel.sortByPriorityLow().observe(this, Observer { todoListAdapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean { // triggered when we typing text and press enter in search view
        if (query != null) {
            searchQueryInDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean { // triggered when we try typing something in search view
        if (newText != null) {
            searchQueryInDatabase(newText)
        }
        return true
    }

    private fun searchQueryInDatabase(query: String) {
        val searchQuery = "%$query%"
        todoViewModel.searchTodo(searchQuery).observe(viewLifecycleOwner, Observer { resultList ->
            todoListAdapter.setData(resultList)
        })
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
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
                restoreDeletedItem(viewHolder.itemView, deletedItem)
            }
        }
        // attach item touch helper to recyclerView
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun restoreDeletedItem(view: View, deletedItem: TodoModel) {
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
