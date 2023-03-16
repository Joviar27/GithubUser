package com.example.githubuser

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.FragmentUserlistBinding

class FollowFragment : Fragment() {

    private var _binding : FragmentUserlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FollowViewModel>()

    companion object {
        const val EXTRA_NAME = "User name"
        const val EXTRA_TYPE = "Type of Data"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserlistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments?.getInt(EXTRA_TYPE, 0 )
        val name = arguments?.getString(EXTRA_NAME)

        Log.d(TAG, "Type $type, Name $name")

        if(type!=null && name!= null){
            viewModel.getFollow(name, type)
            observe()
        }
    }

    private fun observe(){
        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        viewModel.userList.observe(viewLifecycleOwner){
            if (it != null) {
                showRecyclerView(it)
            }
        }
    }

    private fun showRecyclerView(userList: List<User>){
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())

        val userAdapter = UserAdapter(userList) {
            Log.i(TAG, "User Terpilih : $it")
        }
        binding.rvUser.adapter = userAdapter
    }

    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}