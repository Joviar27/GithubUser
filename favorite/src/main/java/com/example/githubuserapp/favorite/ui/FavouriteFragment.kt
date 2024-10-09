package com.example.githubuserapp.favorite.ui

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentUserlistBinding
import com.example.githubuser.ui.BaseFragment
import com.example.core.ui.ListType
import com.example.core.ui.UserAdapter
import com.example.githubuser.di.FavoriteModuleDependencies
import com.example.githubuser.ui.main.MainActivity
import com.example.githubuser.ui.utils.showToast
import com.example.githubuserapp.favorite.ViewModelFactory
import com.example.githubuserapp.favorite.di.DaggerFavoriteComponent
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class FavouriteFragment : BaseFragment<FragmentUserlistBinding, FavouriteViewModel>(
    FragmentUserlistBinding::inflate
), MenuProvider {

    @Inject
    lateinit var factory: ViewModelFactory

    private val favouriteViewModel: FavouriteViewModel by viewModels{
        factory
    }

    private val userAdapter by lazy { UserAdapter(ListType.USER) }

    private fun inject(){
        DaggerFavoriteComponent.builder()
            .context(requireContext())
            .appDependencies(EntryPointAccessors.fromApplication(
                requireActivity().applicationContext,
                FavoriteModuleDependencies::class.java
            ))
            .build()
            .inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

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
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://com.example.githubuser/fragment_detail/${it.id}/${it.login}/true".toUri())
                .build()
            findNavController().navigate(request)
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

        favouriteViewModel.darkMode.observe(viewLifecycleOwner){
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
            R.id.theme -> {
                favouriteViewModel.switchThemeSetting().observe(this){
                    when(it){
                        is Resource.Error -> requireActivity().showToast(
                            getString(R.string.error_theme)
                        )
                        is Resource.Success ->{
                            favouriteViewModel.getThemeSetting()
                            if(requireActivity() is MainActivity){
                                (requireActivity() as MainActivity).updateThemeSetting()
                            }
                        }
                        else -> Unit
                    }
                }
            }
            android.R.id.home -> findNavController().navigateUp()
        }
        return true
    }
}