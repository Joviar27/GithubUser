package com.example.githubuser.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.FragmentUserlistBinding

private  val Context.dataStore :
        DataStore<Preferences> by preferencesDataStore(name = "Setting" )

class FollowFragment : Fragment() {

    private var _binding : FragmentUserlistBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserlistBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = obtainViewModel()

        val type = arguments?.getInt(EXTRA_TYPE, 0 )
        val name = arguments?.getString(EXTRA_NAME)

        Log.d(TAG, "Follow fragment : Type $type, Name $name")

        val followAdapter = FollowAdapter()

        if(type!=null && name!=null){
            if(type== TAB_FOLLOWERS){
                viewModel.getFollower(name).observe(viewLifecycleOwner){ result->
                    when(result){
                        is com.example.githubuser.data.Result.Loading ->{
                            showLoading(true)
                        }
                        is com.example.githubuser.data.Result.Success->{
                            showLoading(false)
                            val followerData = result.data
                            followAdapter.submitList(followerData)
                        }
                        is com.example.githubuser.data.Result.Error ->{
                            showLoading(true)
                            Toast.makeText(
                                context,
                                "Terjadi kesalahan ${result.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    binding?.rvUser?.apply {
                        layoutManager = LinearLayoutManager(requireActivity())
                        setHasFixedSize(true)
                        adapter = followAdapter
                    }
                }
            }
            else if(type== TAB_FOLLOWING){
                viewModel.getFollowing(name).observe(viewLifecycleOwner){ result->
                    when(result){
                        is com.example.githubuser.data.Result.Loading ->{
                            showLoading(true)
                        }
                        is com.example.githubuser.data.Result.Success->{
                            showLoading(false)
                            val followingData = result.data
                            followAdapter.submitList(followingData)
                        }
                        is com.example.githubuser.data.Result.Error ->{
                            showLoading(true)
                            Toast.makeText(
                                context,
                                "Terjadi kesalahan ${result.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    binding?.rvUser?.apply {
                        layoutManager = LinearLayoutManager(requireActivity())
                        setHasFixedSize(true)
                        adapter = followAdapter
                    }
                }
            }
        }
    }

    private fun obtainViewModel() : UserViewModel {
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), requireActivity().dataStore)
        val viewModel : UserViewModel by viewModels {
            factory
        }
        return viewModel
    }

    private fun showLoading(isLoading : Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_NAME = "User name"
        const val EXTRA_TYPE = "Type of Data"

        const val TAB_FOLLOWERS = 0
        const val TAB_FOLLOWING = 1
    }

}