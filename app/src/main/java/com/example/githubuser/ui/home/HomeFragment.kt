package com.example.githubuser.ui.home

import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.Resource
import com.example.githubuser.databinding.FragmentUserlistBinding
import com.example.githubuser.ui.BaseFragment
import com.example.githubuser.ui.component.ListType
import com.example.githubuser.ui.component.UserAdapter
import com.example.githubuser.ui.main.MainActivity
import com.example.githubuser.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentUserlistBinding, HomeViewModel>(
    FragmentUserlistBinding::inflate
), MenuProvider {

    private val homeViewModel: HomeViewModel by viewModels()

    private val userAdapter by lazy { UserAdapter(ListType.USER) }
    private val menuHost: MenuHost by lazy { requireActivity() }
    private val searchManager by lazy {
        requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
    }

    override fun FragmentUserlistBinding.initialize() {
        menuHost.addMenuProvider( this@HomeFragment, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        userAdapter.onItemClicked = {
            val action = HomeFragmentDirections.actionUserListFragmentToDetailFragment(it)
            action.user = it
            findNavController().navigate(action)
        }
        userAdapter.onBookmarkClicked = {
            if (it.isBookmarked==true) {
                homeViewModel.deleteBookmarkedUser(it.id).observe(viewLifecycleOwner){result ->
                    when(result){
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
            } else {
                homeViewModel.setBookmarkedUser(it).observe(viewLifecycleOwner){result ->
                    when(result){
                        is Resource.Loading -> Unit
                        is Resource.Success -> Toast.makeText(
                            requireContext(),
                            "Added to favourite",
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

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }

    override fun observeData() {
        homeViewModel.query.observe(viewLifecycleOwner){
            homeViewModel.getUserList().observe(viewLifecycleOwner){ result ->
                when(result){
                    is Resource.Loading ->{
                        showLoading(true)
                    }
                    is Resource.Success ->{
                        showLoading(false)
                        val userData = result.data
                        userAdapter.submitList(userData)
                    }
                    is Resource.Error ->{
                        showLoading(false)
                        Toast.makeText(
                            context,
                            "Something wrong ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        homeViewModel.updateQuery(null)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main,menu)

        homeViewModel.darkMode.observe(viewLifecycleOwner){
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

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                homeViewModel.updateQuery(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                homeViewModel.updateQuery(newText)
                return false
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.favourite -> {
                val toFavourite =
                    HomeFragmentDirections.actionUserListFragmentToFavouriteFragment()
                view?.findNavController()?.navigate(toFavourite)
            }
            R.id.theme ->{
                homeViewModel.switchThemeSetting().observe(viewLifecycleOwner){
                    when(it){
                        is Resource.Error -> requireActivity().showToast(
                            getString(R.string.error_theme)
                        )
                        is Resource.Success ->{
                            homeViewModel.getThemeSetting()
                            if(requireActivity() is MainActivity){
                                (requireActivity() as MainActivity).updateThemeSetting()
                            }
                        }
                        else -> Unit
                    }
                }
            }
        }
        return true
    }

    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}