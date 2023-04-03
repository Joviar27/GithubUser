package com.example.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.databinding.FragmentUserlistBinding


private  val Context.dataStore :
        DataStore<Preferences> by preferencesDataStore(name = "Setting" )

class HomeFragment : Fragment(), MenuProvider {

    private var _binding : FragmentUserlistBinding? = null
    private val binding get() = _binding

    private var _userAdapter : UserAdapter? = null
    private val userAdapter get() = _userAdapter

    private var darkMode : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserlistBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel : UserViewModel = obtainViewModel()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        _userAdapter = UserAdapter(object : UserAdapter.OnItemClicked{
            override fun onClicked(user : UserEntity) {
                val toDetailUser = HomeFragmentDirections.actionUserListFragmentToDetailFragment(user)
                toDetailUser.user = user
                view.findNavController().navigate(toDetailUser)
            }
        }, object : UserAdapter.OnBookmarkClick{
            override fun onBookmarked(user: UserEntity) {
                if(user.isBookmarked){
                    viewModel.deleteBookmarkedUser(user.id)
                }
                else{
                    viewModel.setBookmarkedUser(user)
                }
            }
        })
        getUserList(DEFAULT_NAME)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main,menu)

        val viewModel : UserViewModel = obtainViewModel()

        viewModel.getThemeSetting().observe(viewLifecycleOwner){
            if(it){
                menu.findItem(R.id.theme).icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_wb_sunny_24, context?.theme)
                darkMode = true
            }
            else{
                darkMode = false
                menu.findItem(R.id.theme).icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_dark_mode_24, context?.theme)
            }
        }

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    getUserList(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.isNotEmpty()) {
                        getUserList(newText)
                    }
                }
                return false
            }
        })
    }

    private fun getUserList(searchQuery : String){

        val viewModel : UserViewModel = obtainViewModel()

        viewModel.getUserList(searchQuery).observe(viewLifecycleOwner){ result ->
            if(result!=null){
                when(result){
                    is com.example.githubuser.data.Result.Loading ->{
                        showLoading(true)
                    }
                    is com.example.githubuser.data.Result.Success ->{
                        showLoading(false)
                        val userData = result.data
                        userAdapter?.submitList(userData)
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
            binding?.rvUser?.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                setHasFixedSize(true)
                adapter = userAdapter
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

        val viewModel : UserViewModel = obtainViewModel()

        when(menuItem.itemId){
            R.id.favourite -> {
                val toFavourite = HomeFragmentDirections.actionUserListFragmentToFavouriteFragment()
                view?.findNavController()?.navigate(toFavourite)
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
        }
        return true
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

    companion object{
        private const val DEFAULT_NAME = "A"
    }
}