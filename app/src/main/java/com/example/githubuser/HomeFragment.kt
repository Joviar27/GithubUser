package com.example.githubuser

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.FragmentUserlistBinding


class HomeFragment : Fragment(), MenuProvider {

    private var _binding : FragmentUserlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UserListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserlistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        observe()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main,menu)

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.getUserList(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.getUserList(newText)
                }
                return false
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }

    private fun observe(){
        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        viewModel.userList.observe(viewLifecycleOwner){
            showRecyclerView(it)
        }
    }

    private fun showRecyclerView(userList: List<User>){
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())

        val userAdapter = UserAdapter(userList) {
            val toDetailUser = HomeFragmentDirections.actionUserListFragmentToDetailFragment()
            toDetailUser.name = it
            view?.findNavController()?.navigate(toDetailUser)
        }
        binding.rvUser.adapter = userAdapter
    }

    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}