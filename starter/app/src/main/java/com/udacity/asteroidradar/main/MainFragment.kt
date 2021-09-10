package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidSelection
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener {
            println("Clicked {${it.codename}")
            viewModel.navigateToAsteroidDetails(it)
        })

        viewModel.shouldNavigateToAsteroidDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                Navigation.findNavController(requireView()).navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.finishedNavigatingToAsteroidDetails()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.setFilter(
            when(item.itemId) {
                R.id.show_all_asteroids -> AsteroidSelection.ALL
                R.id.show_next_week_asteroids -> AsteroidSelection.NEXT_WEEK
                R.id.show_today_asteroids -> AsteroidSelection.TODAY
                else -> AsteroidSelection.ALL
            }
        )
        return true
    }
}
