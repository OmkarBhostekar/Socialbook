package com.example.instaclone.ui.polls.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.comman.Constants
import com.example.instaclone.comman.Resource
import com.example.instaclone.data.DataStore
import com.example.instaclone.databinding.FragmentNewPollBinding
import com.example.instaclone.ui.polls.PollViewModel
import com.example.instaclone.ui.polls.adapters.AddOptionAdapter
import com.example.instaclone.ui.polls.models.NewOption
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewPollFragment : Fragment(R.layout.fragment_new_poll),AddOptionAdapter.OnClickListener{

    private var _binding: FragmentNewPollBinding? = null
    private val binding: FragmentNewPollBinding
        get() = _binding!!
    lateinit var addOptionAdapter: AddOptionAdapter
    private val options = mutableListOf<NewOption>()
    private val viewModel: PollViewModel by viewModels()

    @Inject
    lateinit var dataStore: androidx.datastore.core.DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewPollBinding.bind(view)

        addOptionAdapter = AddOptionAdapter(this)

        binding.apply {
            rvOptions.adapter = addOptionAdapter
            btnAdd.setOnClickListener {
                Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
                if (etNewOption.text.toString().isNotEmpty()){
                    options.add(NewOption(etNewOption.text.toString()))
                    addOptionAdapter.list = options
                    etNewOption.setText("")
                }
            }
            btnSave.setOnClickListener {
                if (etCaption.text.toString().isEmpty()){
                    Toast.makeText(requireContext(), "Please Enter Question", Toast.LENGTH_SHORT).show()
                }else if (options.size < 2 || options.size > 4){
                    Toast.makeText(requireContext(), "Options must be min 2 and max 4", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.createPoll(etCaption.text.toString(),options)
                }
            }
        }

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            Glide.with(requireContext())
                .load(DataStore.readStringFromDS(Constants.USER_IMAGE, dataStore))
                .into(binding.ivProfileImage)
        }

        viewModel.pollCreated.observe(viewLifecycleOwner,{
            when(it){
                is Resource.Success -> {
                    Toast.makeText(requireContext(), it.data!!, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.pollsFragment)
                }
            }
        })
    }

    override fun onDeleteClick(position: Int) {
        options.removeAt(position)
        addOptionAdapter.list = options
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}