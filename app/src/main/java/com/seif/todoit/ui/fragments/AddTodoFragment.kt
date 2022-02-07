package com.seif.todoit.ui.fragments

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
import com.seif.todoit.databinding.FragmentAddTodoBinding
import com.seif.todoit.veiwModel.ShareViewModel
import com.seif.todoit.veiwModel.TodoViewModel


class AddTodoFragment : Fragment() {
    private lateinit var binding: FragmentAddTodoBinding
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var sharedViewModel: ShareViewModel
    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "AddTodoFragment"
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
        // banner ad
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adViewAdd.loadAd(adRequest)
        // set Menu
        setHasOptionsMenu(true)
        binding.prioritySpinner.onItemSelectedListener = sharedViewModel.listener

        // intersttial ad
        val adRequest2 = AdRequest.Builder().build()

        InterstitialAd.load(
            requireContext(),
            getString(R.string.interstitial_ad),
            adRequest2,
            object : InterstitialAdLoadCallback() {
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
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDatabase() {
        val todoTitle = binding.titleEdit.text.toString()
        val priority = binding.prioritySpinner.selectedItem.toString()
        val todoDescription = binding.descriptionEdit.text.toString()
        if (sharedViewModel.validateTodo(todoTitle, todoDescription)) {
            // show interstitial ad
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
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

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Log.d(TAG, "Ad failed to show.")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                    mInterstitialAd = null
                }
            }
        } else {
            Toast.makeText(context, "please fill out all the fields", Toast.LENGTH_SHORT).show()
        }
    }
}