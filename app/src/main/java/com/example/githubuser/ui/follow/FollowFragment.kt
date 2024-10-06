package com.example.githubuser.ui.follow

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.githubuser.databinding.FragmentUserlistBinding
import com.example.githubuser.ui.BaseFragment
import com.example.core.ui.ListType
import com.example.core.ui.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowFragment : BaseFragment<FragmentUserlistBinding, FollowViewModel>(
    FragmentUserlistBinding::inflate
) {

    private val followViewModel: FollowViewModel by viewModels()
    private val followAdapter by lazy { UserAdapter(ListType.FOLLOW) }

    private val type by lazy {
        arguments?.getInt(EXTRA_TYPE, 0 )
    }

    private val name by lazy {
        arguments?.getString(EXTRA_NAME)
    }

    override fun FragmentUserlistBinding.initialize() {
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = followAdapter
        }
    }

    override fun observeData() {
        followViewModel.updateUserName(name ?: "")
        followViewModel.updateTabType(when(type){
            TabType.FOLLOWING.position -> TabType.FOLLOWING
            TabType.FOLLOWER.position -> TabType.FOLLOWER
            else -> TabType.FOLLOWER
        })

        followViewModel.getUserList().observe(viewLifecycleOwner){ result->
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

    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_NAME = "User name"
        const val EXTRA_TYPE = "Type of Data"
    }
}