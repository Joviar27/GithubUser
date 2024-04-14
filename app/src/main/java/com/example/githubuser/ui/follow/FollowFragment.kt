package com.example.githubuser.ui.follow

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.Resource
import com.example.githubuser.databinding.FragmentUserlistBinding
import com.example.githubuser.ui.BaseFragment
import com.example.githubuser.ui.component.ListType
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.component.UserAdapter

class FollowFragment : BaseFragment<FragmentUserlistBinding, FollowViewModel>(
    FragmentUserlistBinding::inflate
) {

//    override val viewModel: FollowViewModel by lazy { obtainViewModel() }
    private lateinit var viewModel: FollowViewModel

    private val followAdapter by lazy { UserAdapter(ListType.FOLLOW) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = obtainViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun FragmentUserlistBinding.initialize() {
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = followAdapter
        }
    }

    override fun observeData() {
        val type = arguments?.getInt(EXTRA_TYPE, 0 )
        val name = arguments?.getString(EXTRA_NAME)

        Log.d(ContentValues.TAG, "Follow fragment : Type $type, Name $name")

        viewModel.updateUserName(name ?: "")
        viewModel.updateTabType(when(type){
            TabType.FOLLOWING.position -> TabType.FOLLOWING
            TabType.FOLLOWER.position -> TabType.FOLLOWER
            else -> TabType.FOLLOWER
        })

        viewModel.getUserList().observe(viewLifecycleOwner){ result->
            when(result){
                is Resource.Loading ->{
                    showLoading(true)
                }
                is Resource.Success->{
                    showLoading(false)
                    followAdapter.submitList(result.data)
                }
                is Resource.Error ->{
                    showLoading(true)
                    Toast.makeText(
                        context,
                        "Something Wrong ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun obtainViewModel() : FollowViewModel {
        val factory : ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity())
        val viewModel : FollowViewModel by viewModels {
            factory
        }
        return viewModel
    }

    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_NAME = "User name"
        const val EXTRA_TYPE = "Type of Data"
    }
}