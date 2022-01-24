package com.acamara.scanme.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acamara.scanme.R
import com.acamara.scanme.adapters.ProductRecyclerAdapterListing
import com.acamara.scanme.data_models.Product_model
import com.acamara.scanme.ui.home.HomeViewModel

class HistoryCategoryFragment : Fragment() {
    private lateinit var arg: String
    lateinit var recyclerView:RecyclerView
    var ARG = "category"
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        arguments?.getString(ARG) ?: ""
        val root = inflater.inflate(R.layout.fragment_view_pager, container, false)
        recyclerView = root.findViewById(R.id.history_recycler_view)
        //root.findViewById<TextView>(R.id.title).text = arguments?.get("category").toString()
        return root
    }
    companion object {
        fun newInstance(arg: String) =
                HistoryCategoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG, arg)
                    }
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parsedList = ArrayList<Product_model>()
        var adapter = ProductRecyclerAdapterListing()
        var linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        homeViewModel.productList.observe(viewLifecycleOwner, Observer { productList ->
            if (productList.isNotEmpty()) {

            } else {

            }
            System.out.println(productList.size)
            for (i in 0..productList.size - 1) {
                if (productList[i].type.equals(arguments?.get("category").toString()) || productList[i].type.contains(arguments?.get("category").toString())) {
                    parsedList.add(productList[i])
                }
            }
            productList.let {
                adapter.setProduct(parsedList)
            }
        })
    }
}