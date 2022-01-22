package com.seif.todoit.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.seif.todoit.R
import com.seif.todoit.data.models.TodoModel
import com.seif.todoit.databinding.FragmentUpdateTodoBinding
import com.seif.todoit.ui.fragments.UpdateTodoFragmentArgs.Companion.fromBundle
import com.seif.todoit.veiwModel.ShareViewModel
import com.seif.todoit.veiwModel.TodoViewModel


class UpdateTodoFragment : Fragment() {
    private lateinit var binding: FragmentUpdateTodoBinding
    private lateinit var shareViewModel: ShareViewModel
    private lateinit var todoViewModel: TodoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shareViewModel = ViewModelProvider(requireActivity())[ShareViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
        // set Menu
        setHasOptionsMenu(true)
        binding.editTitleUpdate.setText(fromBundle(requireArguments()).currentTodo.title)
        binding.editDescriptionUpdate.setText(fromBundle(requireArguments()).currentTodo.description)
        binding.spinnerUpdate.setSelection(
            shareViewModel.parsePriorityToInt(
                fromBundle(
                    requireArguments()
                ).currentTodo.priority
            )
        )
        binding.spinnerUpdate.onItemSelectedListener = shareViewModel.listener
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            updateTodoItem()
        } else if (item.itemId == R.id.menu_delete) {
            deleteTodoItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteTodoItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Ok") { _, _ ->
            todoViewModel.deleteTodo(fromBundle(requireArguments()).currentTodo)
            Toast.makeText(requireContext(), "deleted Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateTodoFragment_to_toDoListFragment)
        }
        with(builder) {
            setNegativeButton("Cancel") { _, _ -> }
            setTitle("Delete '${fromBundle(requireArguments()).currentTodo.title}' ?")
            setMessage("Are you sure you want to delete '${fromBundle(requireArguments()).currentTodo.title}' ?")
            create().show()
        }
    }

    private fun updateTodoItem() {
        val currentTitle = binding.editTitleUpdate.text.toString()
        val currentPriority = binding.spinnerUpdate.selectedItem.toString()
        val currentDescription = binding.editDescriptionUpdate.text.toString()
        if (shareViewModel.validateTodo(currentTitle, currentDescription)) {
            val updateTodo = TodoModel(
                fromBundle(requireArguments()).currentTodo.id,
                currentTitle,
                shareViewModel.getPriority(currentPriority),
                currentDescription
            )
            todoViewModel.updateTodo(updateTodo)
            findNavController().navigate(R.id.action_updateTodoFragment_to_toDoListFragment)
            Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}