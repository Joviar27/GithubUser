package com.example.githubuser.ui.favourite

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.Resource
import com.example.githubuser.databinding.FragmentUserlistBinding
import com.example.githubuser.ui.BaseFragment
import com.example.githubuser.ui.component.ListType
import com.example.githubuser.ui.component.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentUserlistBinding, FavouriteViewModel>(
    FragmentUserlistBinding::inflate
), MenuProvider {

    private val favouriteViewModel: FavouriteViewModel by viewModels()
    private val userAdapter by lazy { UserAdapter(ListType.USER) }

    override fun FragmentUserlistBinding.initialize() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this@FavouriteFragment, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = userAdapter
        }

        userAdapter.onItemClicked = {
            val toDetailUser =
                FavouriteFragmentDirections.actionFavouriteFragmentToDetailFragment(it)
            toDetailUser.user = it
            findNavController().navigate(toDetailUser)
        }
        userAdapter.onBookmarkClicked = {
            favouriteViewModel.deleteBookmarkedUser(it.id).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> Toast.makeText(
                        requireContext(),
                        "Removed from favourite",
                        Toast.LENGTH_SHORT
                    ).show()

                    is Resource.Error -> Toast.makeText(
                        requireContext(),
                        "Something Wrong ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun observeData() {
        favouriteViewModel.getFavouriteList().observe(viewLifecycleOwner){ result ->
            when(result){
                is Resource.Loading -> showLoading(true)
                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        requireContext(),
                        "Something wrong ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Success ->{
                    showLoading(false)
                    userAdapter.submitList(result.data)
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_favourite,menu)

        favouriteViewModel.getThemeSetting().observe(viewLifecycleOwner){
            if(it){
                menu.findItem(R.id.theme).icon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_wb_sunny_24,
                    context?.theme
                )
            }
            else{
                menu.findItem(R.id.theme).icon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_dark_mode_24,
                    context?.theme
                )
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId){
            R.id.theme -> favouriteViewModel.switchThemeSetting()
            android.R.id.home -> findNavController().navigateUp()
        }
        return true
    }
}