package com.example.githubuser.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.databinding.FragmentUserlistBinding

private  val Context.dataStore :
        DataStore<Preferences> by preferencesDataStore(name = "Setting" )

class FavouriteFragment : Fragment(), MenuProvider {

    private var _binding : FragmentUserlistBinding? = null
    private val binding get() = _binding

    private var _userAdapter : UserAdapter? = null
    private val userAdapter get() = _userAdapter

    private var darkMode : Boolean = true

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

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        _userAdapter = UserAdapter(object : UserAdapter.OnItemClicked{
            override fun onClicked(user : UserEntity) {
                val toDetailUser = FavouriteFragmentDirections.actionFavouriteFragmentToDetailFragment(user)
                toDetailUser.user = user
                view.findNavController().navigate(toDetailUser)
            }
        }, object : UserAdapter.OnBookmarkClick{
            override fun onBookmarked(user: UserEntity) {
                viewModel.deleteBookmarkedUser(user.id)
            }
        })
        getBookmarkedUser()
    }

    private fun getBookmarkedUser(){
        val viewModel = obtainViewModel()
        showLoading(true)
        viewModel.getBookmarkedUser().observe(viewLifecycleOwner){ listBookmarked ->
            val favouriteUser = listBookmarked.map { bookmarked ->
                UserEntity(
                    id = bookmarked.id,
                    login = bookmarked.login,
                    avatar_url = bookmarked.avatar_url,
                    followers = bookmarked.followers,
                    following = bookmarked.following,
                    location = bookmarked.location,
                    name = bookmarked.name,
                    isBookmarked = true
                )
            }
            userAdapter?.submitList(favouriteUser)
            showLoading(false)
        }
        binding?.rvUser?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }

    private fun obtainViewModel() : UserViewModel{
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), requireActivity().dataStore)
        val viewModel : UserViewModel  by viewModels {
            factory
        }
        return viewModel
    }

    private fun showLoading(isLoading : Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_favourite,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val viewModel = obtainViewModel()

        when (menuItem.itemId){
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
}