package com.seif.todoit.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
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
    private var mInterstitialAd: InterstitialAd? = null
    private  var TAG = "UpdateTodoFragment"

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

        // banner ad
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adViewUpdate.loadAd(adRequest)
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

        // intersttial ad
        val adRequest2 = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),"ca-app-pub-3940256099942544/1033173712", adRequest2, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateTodoItem()
            R.id.menu_share_todo -> shareTodo()
            R.id.menu_delete -> deleteTodoItem()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun shareTodo() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        val currentTodoTitle = binding.editTitleUpdate.text.toString()
        val currentTodoDescription = binding.editDescriptionUpdate.text.toString()
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "• $currentTodoTitle\n\n" +
                    currentTodoDescription
        )
        shareIntent.type = "text/plain"
        startActivity(
            Intent.createChooser(
                shareIntent,
                "Choose the app you want to share todo with:"
            )
        )
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
            // show interstitial ad
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                    // update in database
                    val updateTodo = TodoModel(
                        fromBundle(requireArguments()).currentTodo.id,
                        currentTitle,
                        shareViewModel.getPriority(currentPriority),
                        currentDescription
                    )
                    todoViewModel.updateTodo(updateTodo)
                    findNavController().navigate(R.id.action_updateTodoFragment_to_toDoListFragment)
                    Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Log.d(TAG, "Ad failed to show.")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                    mInterstitialAd = null
                }
            }

        } else {
            Toast.makeText(requireContext(), "please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}