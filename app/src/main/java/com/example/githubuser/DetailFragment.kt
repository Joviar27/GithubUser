package com.example.githubuser

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.githubuser.databinding.FragmentDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {

    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser(DetailFragmentArgs.fromBundle(arguments as Bundle).name)

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        viewModel.userData.observe(viewLifecycleOwner){
            if (it != null) {
                binding.tvName.text = it.name
                binding.tvUsername.text =it.login
                binding.tvCity.text = it.location

                Glide.with(requireActivity())
                    .load(it.avatarUrl)
                    .placeholder(R.color.tangerine)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivProfile)

                showTab(it.followers, it.following, it.login)
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showTab(followers : Int, following : Int, name: String){

        val sectionsPagerAdapter = SectionPagerAdapter(requireActivity())
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position ->
            when(position+1){
                1 -> {
                    tab.text = "Followers\n $followers"
                    sectionsPagerAdapter.type = position
                }
                2 -> {
                    tab.text = "Following\n $following"
                    sectionsPagerAdapter.type = position
                }
                else -> {
                    tab.text = "Follow"
                }
            }

        }.attach()

        sectionsPagerAdapter.user = name
        Log.d(TAG, "User di detail fragment : $name")
    }
}