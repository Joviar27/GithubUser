package com.example.githubuser.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.githubuser.R
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.databinding.FragmentDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

private  val Context.dataStore :
        DataStore<Preferences> by preferencesDataStore(name = "Setting" )

class DetailFragment : Fragment(), MenuProvider {

    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding

    private var darkMode : Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = obtainViewModel()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        val user = DetailFragmentArgs.fromBundle(arguments as Bundle).user
        viewModel.getDetailUser(user.login).observe(viewLifecycleOwner){ result ->
            when(result){
                is com.example.githubuser.data.Result.Loading ->{
                    showLoading(true)
                }
                is com.example.githubuser.data.Result.Success ->{
                    showLoading(false)
                    result.data.map {
                        bind(it)
                    }
                }
                is com.example.githubuser.data.Result.Error ->{
                    showLoading(false)
                    Toast.makeText(
                        context,
                        "Terjadi kesalahan ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun bind (user : UserEntity){
        binding?.tvName?.text = user.name
        binding?.tvUsername?.text = user.login
        binding?.tvCity?.text = user.location

        Glide.with(requireActivity())
            .load(user.avatar_url)
            .placeholder(R.color.tangerine)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding!!.ivProfile)

        showTab(user.followers, user.following, user.login)
    }

    private fun showTab(followers : Int, following : Int, name: String){

        val sectionsPagerAdapter = SectionPagerAdapter(requireActivity())
        binding?.viewPager?.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding!!.tabLayout,binding!!.viewPager){tab,position ->
            when(position){
                FollowFragment.TAB_FOLLOWERS -> {
                    tab.text = "${resources.getString(TAB_TITLES[position])}\n $followers"
                }
                FollowFragment.TAB_FOLLOWING -> {
                    tab.text = "${resources.getString(TAB_TITLES[position])}\n $following"
                }
                else -> {
                    tab.text = "Follow"
                }
            }
        }.attach()

        sectionsPagerAdapter.user = name
    }

    private fun obtainViewModel() : UserViewModel{
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), requireActivity().dataStore)
        val viewModel : UserViewModel  by viewModels {
            factory
        }
        return viewModel
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_detail,menu)
        val user = DetailFragmentArgs.fromBundle(arguments as Bundle).user
        if(user.isBookmarked){
            menu.findItem(R.id.update_favourite).title = getString(R.string.remove_from_favourite)
        }
        else{
            menu.findItem(R.id.update_favourite).title = getString(R.string.add_to_favourite)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val viewModel = obtainViewModel()
        val user = DetailFragmentArgs.fromBundle(arguments as Bundle).user
        when (menuItem.itemId){
            R.id.share ->{
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, "https://github.com/${user.login}")
                    type = "text/plain"
                }
                startActivity(shareIntent)
            }
            R.id.update_favourite ->{
                if(user.isBookmarked){
                    viewModel.deleteBookmarkedUser(user.id)
                    user.isBookmarked = false
                }
                else{
                    viewModel.setBookmarkedUser(user)
                    user.isBookmarked = true
                }
            }
            R.id.theme ->{
                viewModel.saveThemeSetting(!darkMode)
                if (darkMode){
                    menuItem.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_dark_mode_24, context?.theme)
                }
                else{
                    menuItem.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_wb_sunny_24, context?.theme)
                }
            }
            android.R.id.home ->{
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