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
import com.example.core.data.Resource
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentUserlistBinding
import com.example.githubuser.ui.BaseFragment
import com.example.core.ui.ListType
import com.example.core.ui.UserAdapter
import com.example.githubuser.ui.main.MainActivity
import com.example.githubuser.ui.utils.showToast
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
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
    private lateinit var searchView: SearchView

    override fun FragmentUserlistBinding.initialize() {
        menuHost.addMenuProvider( this@HomeFragment, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        userAdapter.onItemClicked = {
            val action = HomeFragmentDirections.actionUserListFragmentToDetailFragment(
                it.id, it.login, it.isBookmarked ?: false
            )
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
        homeViewModel.userList.observe(viewLifecycleOwner){ result ->
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
                    requireActivity().showToast(
                        getString(R.string.error_general, result.error)
                    )
                }
            }
        }
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

        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                homeViewModel.getUserList(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                homeViewModel.getUserList(newText)
                return false
            }
        })
    }

    override fun onPause() {
        super.onPause()
        if(::searchView.isInitialized){
            searchView.setOnQueryTextListener(null)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.favourite -> {
                installFavoriteFeature()
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

    private fun installFavoriteFeature() {
        val favoriteModule = "favorite"
        val manager = SplitInstallManagerFactory.create(requireContext())
        if (manager.installedModules.contains(favoriteModule)) {
            navigateToFavorite()
        } else {
            val request = SplitInstallRequest.newBuilder()
                .addModule(favoriteModule)
                .build()
            val listener = SplitInstallStateUpdatedListener {
                when (it.status()) {
                    SplitInstallSessionStatus.DOWNLOADING ->{
                        showLoading(true)
                    }
                    SplitInstallSessionStatus.INSTALLED -> {
                        navigateToFavorite()
                    }
                    SplitInstallSessionStatus.FAILED -> {
                        showLoading(false)
                        showFailedInstallModuleError()
                    }
                    else -> Unit
                }
            }
            manager.registerListener(listener)
            manager.startInstall(request).addOnFailureListener {
                showLoading(false)
                showFailedInstallModuleError()
            }
        }
    }

    private fun navigateToFavorite(){
        try {
            val toFavourite =
                HomeFragmentDirections.actionHomeFragmentToFavoriteNavigation()
            view?.findNavController()?.navigate(toFavourite)
        }catch (e: Exception){
            showFailedInstallModuleError()
        }
    }

    private fun showFailedInstallModuleError(){
        Toast.makeText(requireContext(), "Failed to install module", Toast.LENGTH_SHORT).show()
    }


    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}