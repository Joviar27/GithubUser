package com.example.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.githubuser.R
import com.example.githubuser.data.Resource
import com.example.githubuser.databinding.FragmentDetailBinding
import com.example.githubuser.domain.User
import com.example.githubuser.ui.BaseFragment
import com.example.githubuser.ui.component.SectionPagerAdapter
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.follow.TabType
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>(
    FragmentDetailBinding::inflate
), MenuProvider {

//    override val viewModel: DetailViewModel by lazy { obtainViewModel() }
    private lateinit var viewModel: DetailViewModel

    private val menuHost: MenuHost by lazy { requireActivity() }
    private val sectionPagerAdapter by lazy { SectionPagerAdapter(requireActivity()) }

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = obtainViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun FragmentDetailBinding.initialize() {
        menuHost.addMenuProvider(this@DetailFragment, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
    }

    override fun observeData() {
        user = DetailFragmentArgs.fromBundle(arguments as Bundle).user
        viewModel.updateUserName(user.login)

        if (user.isBookmarked==true) {
            viewModel.getFavouriteDetailUser().observe(viewLifecycleOwner){ result ->
                when(result){
                    is Resource.Loading ->{
                        showLoading(true)
                    }
                    is Resource.Success ->{
                        showLoading(false)
                        bind(result.data)
                        user = result.data
                    }
                    is Resource.Error ->{
                        showLoading(false)
                        Toast.makeText(
                            context,
                            "Something Wrong ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else{
            viewModel.getDetailUser().observe(viewLifecycleOwner){ result ->
                when(result){
                    is Resource.Loading ->{
                        showLoading(true)
                    }
                    is Resource.Success ->{
                        showLoading(false)
                        bind(result.data)
                        user = result.data
                    }
                    is Resource.Error ->{
                        showLoading(false)
                        Toast.makeText(
                            context,
                            "Something Wrong ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun bind (user : User){
        binding.tvName.text = user.name
        binding.tvUsername.text = user.login
        binding.tvCity.text = user.location

        Glide.with(requireActivity())
            .load(user.avatar_url)
            .placeholder(R.color.tangerine)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.ivProfile)

        showTab(user.followers ?: 0, user.following ?: 0, user.login)
    }

    private fun showTab(followers : Int, following : Int, name: String){
        binding.viewPager.adapter = sectionPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                TabType.FOLLOWER.position -> {
                    tab.text = "${resources.getString(TAB_TITLES[position])}\n $followers"
                }
                TabType.FOLLOWING.position -> {
                    tab.text = "${resources.getString(TAB_TITLES[position])}\n $following"
                }
                else -> {
                    tab.text = "Follow"
                }
            }
        }.attach()
        sectionPagerAdapter.user = name
    }

    private fun obtainViewModel() : DetailViewModel {
        val factory : ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity())
        val viewModel : DetailViewModel by viewModels {
            factory
        }
        return viewModel
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_detail,menu)

        viewModel.darkMode.observe(viewLifecycleOwner){
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

        if(user.isBookmarked==true){
            menu.findItem(R.id.update_favourite).title = getString(R.string.remove_from_favourite)
        } else{
            menu.findItem(R.id.update_favourite).title = getString(R.string.add_to_favourite)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId){
            R.id.share ->{
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, "https://github.com/${user.login}")
                    type = "text/plain"
                }
                startActivity(shareIntent)
            }
            R.id.update_favourite ->{
                if (user.isBookmarked==true) {
                    viewModel.deleteBookmarkedUser(user.id).observe(viewLifecycleOwner){result ->
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
                    viewModel.setBookmarkedUser(user).observe(viewLifecycleOwner){result ->
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
            R.id.theme -> viewModel.switchThemeSetting()
            android.R.id.home -> {
                Log.d("UserRepo", "Habis pencet back kok masih get detail user")
                findNavController().navigateUp()
            }
        }
        return true
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.followers, R.string.following)
    }

}