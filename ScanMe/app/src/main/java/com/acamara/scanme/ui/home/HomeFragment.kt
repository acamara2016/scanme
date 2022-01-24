package com.acamara.scanme.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acamara.scanme.Homepage
import com.acamara.scanme.R
import com.acamara.scanme.adapters.ProductRecyclerAdapter
import com.acamara.scanme.api.PriceComparisonAPI
import com.acamara.scanme.database.FirebaseHelper
import com.acamara.scanme.ui.map.MapsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class HomeFragment : Fragment() {
    private lateinit var fb : FirebaseHelper
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        fb = FirebaseHelper()
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //Handler().postDelayed({ load(root) }, 1500)
        root.findViewById<SearchView>(R.id.search_view).setOnSearchClickListener {
            if (root.findViewById<SearchView>(R.id.search_view).query.length==12)
                GlobalScope.async {PriceComparisonAPI().getProduct(root,root.findViewById<SearchView>(R.id.search_view).toString()) }
            else {
                var count = 12-root.findViewById<SearchView>(R.id.search_view).query.length
                root.findViewById<SearchView>(R.id.search_view).queryHint = "Remaining" + count
            }
        }
        //Intent from fragment into Activity
        root.findViewById<FloatingActionButton>(R.id.button_to_map).setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_mapsActivity)
        }
        return root
    }

    private fun load(root: View?) {
        root?.findViewById<ProgressBar>(R.id.loading_home)?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter = ProductRecyclerAdapter()
        var linearLayoutManager = GridLayoutManager(context, 2)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        homeViewModel.productList.observe(viewLifecycleOwner, Observer { productList ->
            if (productList.isNotEmpty()) {
                empty_linear.visibility = View.GONE
                recycler_view_layout.visibility = View.VISIBLE
            } else {
                recycler_view_layout.visibility = View.GONE
                empty_linear.visibility = View.VISIBLE
            }
            System.out.println(productList.size)
            productList.let { adapter.setProduct(it) }
        })
    }

}