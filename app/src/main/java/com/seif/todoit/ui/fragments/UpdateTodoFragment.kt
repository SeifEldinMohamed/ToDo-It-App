package com.seif.todoit.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.seif.todoit.R
import com.seif.todoit.data.models.PriorityModel
import com.seif.todoit.databinding.FragmentUpdateTodoBinding
import com.seif.todoit.ui.fragments.UpdateTodoFragmentArgs.Companion.fromBundle
import com.seif.todoit.ui.veiwmodels.ShareViewModel


class UpdateTodoFragment : Fragment() {
    private lateinit var binding: FragmentUpdateTodoBinding
    private lateinit var shareViewModel: ShareViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shareViewModel = ViewModelProvider(requireActivity())[ShareViewModel::class.java]
        // set Menu
        setHasOptionsMenu(true)
        binding.editTitleUpdate.setText(fromBundle(requireArguments()).currentTodo.title)
        binding.editDescriptionUpdate.setText(fromBundle(requireArguments()).currentTodo.description)
        binding.spinnerUpdate.setSelection(parsePriority(fromBundle(requireArguments()).currentTodo.priority))
        binding.spinnerUpdate.onItemSelectedListener = shareViewModel.listener



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    private fun parsePriority(priorityModel: PriorityModel): Int {
        return when (priorityModel) {
            PriorityModel.HIGH -> 0
            PriorityModel.MEDIUM -> 1
            PriorityModel.LOW -> 2
        }
    }

}